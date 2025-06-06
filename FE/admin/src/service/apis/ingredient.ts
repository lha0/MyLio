import authClient from '@/service/authClient';
import { CustomError, PaginationResponse, Response } from '@/types/apiResponse';
import { IngredientForm, IngredientType } from '@/types/ingredient';

export async function getIngredientList(
  keyword?: string,
  page: number = 1,
  size: number = 10
): Promise<Response<PaginationResponse<IngredientType>>> {
  try {
    const params = keyword ? { keyword, page, size } : { page, size };
    const response = await authClient.get('/ingredient', { params });
    return response.data;
  } catch (error: unknown) {
    if (error instanceof Error) {
      const customError = error as CustomError;
      const errorMessage =
        customError.response?.data?.error?.message || error.message;
      throw new Error(errorMessage);
    }
    throw new Error('unknown error');
  }
}

export async function postIngredient(ingredient: IngredientForm) {
  try {
    const response = await authClient.post('/ingredient', ingredient);
    return response.data;
  } catch (error: unknown) {
    if (error instanceof Error) {
      const customError = error as CustomError;
      const errorMessage =
        customError.response?.data?.error?.message || error.message;
      throw new Error(errorMessage);
    }
    throw new Error('unknown error');
  }
}

export async function patchIngredient(ingredient: IngredientType) {
  try {
    const response = await authClient.patch(
      `/ingredient/${ingredient.ingredientTemplateId}`,
      ingredient
    );
    return response.data;
  } catch (error: unknown) {
    if (error instanceof Error) {
      const customError = error as CustomError;
      const errorMessage =
        customError.response?.data?.error?.message || error.message;
      throw new Error(errorMessage);
    }
    throw new Error('unknown error');
  }
}
