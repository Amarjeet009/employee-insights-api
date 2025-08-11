package com.amarj.controller


import com.amarj.entity.client.SpentSummaryResponse
import com.amarj.service.ClientInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/client")
class ClientInfoController(private val clientInfoService: ClientInfoService) {

    @GetMapping("/getClientDetails")
    fun getClientDetails(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Mono<Map<String, Any>> {
        val usersFlux = clientInfoService.getClientDetails(page, size).collectList()
        val totalMono = clientInfoService.countClient()

        return Mono.zip(usersFlux, totalMono)
            .map { tuple ->
                mapOf(
                    "page" to page,
                    "size" to size,
                    "total" to tuple.t2,
                    "users" to tuple.t1
                )
            }
    }

    @GetMapping("getSpentSummary")
    fun getSpentSummary(@RequestParam groupBy: String): Mono<SpentSummaryResponse> {
        return when (groupBy.lowercase()) {
            "jobandage" -> clientInfoService.getSpentByJobAndAge()
                .collectList()
                .map { SpentSummaryResponse("jobandage", it.map { item -> item as Any }) }

            "job" -> clientInfoService.getSpentAndOrderByJob()
                .collectList()
                .map { SpentSummaryResponse("job", it.map { item -> item as Any }) }

            "hobbies" -> clientInfoService.getSpentAndOrderByHobbies()
                .collectList()
                .map { SpentSummaryResponse("hobbies", it.map { item -> item as Any }) }

            else -> Mono.error(IllegalArgumentException("Invalid groupBy value: $groupBy"))
        }
    }
}