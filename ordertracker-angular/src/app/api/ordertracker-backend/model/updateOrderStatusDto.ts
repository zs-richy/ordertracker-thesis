/**
 * OpenAPI definition
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

export interface UpdateOrderStatusDto { 
    orderId?: number;
    newStatus?: UpdateOrderStatusDto.NewStatusEnum;
}
export namespace UpdateOrderStatusDto {
    export type NewStatusEnum = 'CREATED' | 'WORKING' | 'PREPARING' | 'DELIVERING' | 'COMPLETED';
    export const NewStatusEnum = {
        CREATED: 'CREATED' as NewStatusEnum,
        WORKING: 'WORKING' as NewStatusEnum,
        PREPARING: 'PREPARING' as NewStatusEnum,
        DELIVERING: 'DELIVERING' as NewStatusEnum,
        COMPLETED: 'COMPLETED' as NewStatusEnum
    };
}