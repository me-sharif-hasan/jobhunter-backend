export default class ApiResponse<T> {
    success: boolean = false;
    message: string = "";
    data: T | undefined;
    totalRecords: number | undefined;
}