export interface IApiSuccessResponse<T> {
    success: boolean,
    message: string,
    payload: T
}

export interface IApiErrorResponse {
    timestamp: string,
    status: number,
    error: string,
    message: string,
    path: string,
    validationErrors: Record<string, string>
}