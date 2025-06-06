package com.ssafy.mylio.domain.payment.service;

import com.ssafy.mylio.domain.payment.entity.PaymentMethod;
import com.ssafy.mylio.global.error.code.ErrorCode;
import com.ssafy.mylio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentFacadeService {
    private final List<PayService> payServices;

    public Mono<PayService> resolve(PaymentMethod method) {
        return Mono.defer(() ->
                Mono.justOrEmpty(
                        payServices.stream()
                                .filter(service -> service.getPaymentMethod() == method)
                                .findFirst()
                ).switchIfEmpty(
                        Mono.error(new CustomException(ErrorCode.PAY_NOT_MATCH, "method", method))
                )
        );
    }
}
