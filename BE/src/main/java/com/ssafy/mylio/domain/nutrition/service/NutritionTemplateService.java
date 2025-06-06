package com.ssafy.mylio.domain.nutrition.service;

import com.ssafy.mylio.domain.nutrition.dto.request.NutritionTemplateRequestDto;
import com.ssafy.mylio.domain.nutrition.dto.request.NutritionTemplateUpdateRequestDto;
import com.ssafy.mylio.domain.nutrition.dto.response.NutritionTemplateResponseDto;
import com.ssafy.mylio.domain.nutrition.entity.NutritionTemplate;
import com.ssafy.mylio.domain.nutrition.repository.NutritionTemplateRepository;
import com.ssafy.mylio.domain.store.repository.StoreRepository;
import com.ssafy.mylio.global.common.CustomPage;
import com.ssafy.mylio.global.error.code.ErrorCode;
import com.ssafy.mylio.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NutritionTemplateService {

    private final NutritionTemplateRepository nutritionTemplateRepository;

    public CustomPage<NutritionTemplateResponseDto> getNutritionTemplate(String userType, String keyword, Pageable pageable) {
        // userType 검증
        if(!(userType.equals("SUPER") || userType.equals("STORE"))){
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS, "userType", userType);
        }

        // 영양성분 템플릿 모두 조회
        Page<NutritionTemplate> nutritionTemplates = nutritionTemplateRepository.findAllByKeyword(keyword, pageable);

        return new CustomPage<>(nutritionTemplates.map(NutritionTemplateResponseDto::of));
    }

    @Transactional
    public void addNutritionTemplate(String userType, NutritionTemplateRequestDto requestDto){
        // 관리자인지 검증
        validateSuperAdmin(userType);

        // 이름이 똑같은 영양성분 있는지 조회
        if(nutritionTemplateRepository.existsByNameKr(requestDto.getNutritionTemplateName())){
            throw new CustomException(ErrorCode.NUTRITION_TEMPLATE_ALREADY_EXISTS, "name", requestDto.getNutritionTemplateName());
        }

        // 없다면 영양성분 등록
        NutritionTemplate nutritionTemplate = requestDto.toEntity();
        nutritionTemplateRepository.save(nutritionTemplate);
    }

    @Transactional
    public void updateNutritionTemplate(String userType, Integer nutritionId, NutritionTemplateUpdateRequestDto updateRequestDto){
        // 관리자인지 검증
        validateSuperAdmin(userType);

        // 영양성분 조회
        NutritionTemplate nutritionTemplate = nutritionTemplateRepository.findById(nutritionId)
                .orElseThrow(()-> new CustomException(ErrorCode.NUTRITION_TEMPLATE_NOT_FOUND,"nutritionId", nutritionId));

        // 영양성분 업데이트
        nutritionTemplate.update(
                updateRequestDto.getNutritionTemplateName(),
                updateRequestDto.getNutritionTemplateNameEn(),
                updateRequestDto.getNutritionTemplateType());
    }

    private void validateSuperAdmin(String userType){
        if(!userType.equals("SUPER")){
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS, "userType", userType);
        }
    }
}
