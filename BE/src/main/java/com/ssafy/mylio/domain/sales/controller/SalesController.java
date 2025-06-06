package com.ssafy.mylio.domain.sales.controller;

import com.ssafy.mylio.domain.sales.dto.response.*;
import com.ssafy.mylio.domain.sales.service.SalesService;
import com.ssafy.mylio.global.aop.swagger.ApiErrorCodeExample;
import com.ssafy.mylio.global.aop.swagger.ApiErrorCodeExamples;
import com.ssafy.mylio.global.common.response.CommonResponse;
import com.ssafy.mylio.global.error.code.ErrorCode;
import com.ssafy.mylio.global.security.auth.UserPrincipal;
import com.ssafy.mylio.global.util.AuthenticationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Tag(name = "통계 조회", description = "매출, 카테고리 등의 통계 조회 API")
public class SalesController {

    private final SalesService salesService;
    private final AuthenticationUtil authenticationUtil;

    @GetMapping("/by_category")
    @ApiErrorCodeExamples({ErrorCode.STORE_NOT_FOUND})
    @Operation(summary = "카테고리 별 매출 통계", description = "카테고리를 기준으로 매출 통계를 조회합니다")
    ResponseEntity<CommonResponse<List<CategorySalesResponseDto>>> getCategoryStatistics(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        Integer storeId = authenticationUtil.getCurrntStoreId(userPrincipal);
        return CommonResponse.ok(salesService.getCategorySales(storeId, year, month));
    }

    @GetMapping
    @ApiErrorCodeExamples({ErrorCode.INVALID_ROLE})
    @Operation(summary = "매출 통계",description = "년도별, 월별을 기준으로 매출 통계를 조회합니다.")
    ResponseEntity<CommonResponse<List<SalesResponse>>> getSalesStatistics(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("year") Integer year,
            @RequestParam(value = "month",required = false) Integer month){
        Integer storeId = authenticationUtil.getCurrntStoreId(userPrincipal);
        String userType = authenticationUtil.getCurrntUserType(userPrincipal);
        List<SalesResponse> response = salesService.getSalesStatistics(userType, storeId, year, month);

        return CommonResponse.ok(response);
    }

    @GetMapping("/daily")
    @ApiErrorCodeExamples({})
    @Operation(summary = "당일 매출 / 주문 건수 조회 API", description = "통계 페이지 상단 당일 매출 / 주문 건수를 조회합니다.")
    ResponseEntity<CommonResponse<DailySalesResponseDto>> getDailySales(
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        Integer storeId = authenticationUtil.getCurrntStoreId(userPrincipal);
        String userType = authenticationUtil.getCurrntUserType(userPrincipal);

        return CommonResponse.ok(salesService.getDailySales(storeId,userType));
    }

    @GetMapping("/by_payment")
    @Operation(summary = "결제 수단 별 매출 비율 조회", description = "월별 결제 수단 별 매출 비율을 조회합니다.")
    @ApiErrorCodeExample(ErrorCode.INVALID_ROLE)
    public ResponseEntity<CommonResponse<List<PaymentSalesResponseDto>>> getPaymentMethodRatio(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("year") Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        String userType = authenticationUtil.getCurrntUserType(userPrincipal);
        Integer storeId = authenticationUtil.getCurrntStoreId(userPrincipal);
        return CommonResponse.ok(salesService.getPaymentMethodRatio(userType,storeId, year, month));
    }

    @GetMapping("/by_order_type")
    @Operation(summary = "매장/포장 별 통계 조회 API", description = "매장 / 포장 별 통계를 조회합니다.")
    @ApiErrorCodeExamples({})
    public ResponseEntity<CommonResponse<List<OrderTypeSalesResponseDto>>> getOrderTypeStatics(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("year") Integer year,
            @RequestParam(value = "month", required = false) Integer month){
        Integer storeId = authenticationUtil.getCurrntStoreId(userPrincipal);
        String userType = authenticationUtil.getCurrntUserType(userPrincipal);
        return CommonResponse.ok(salesService.getOrderTypeStatics(userType,storeId,year,month));
    }
}
