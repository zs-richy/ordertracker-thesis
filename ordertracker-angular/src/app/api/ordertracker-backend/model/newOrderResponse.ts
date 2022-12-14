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

export interface NewOrderResponse { 
    errorMessages?: Array<string>;
    orderId?: number;
    orderStatus?: NewOrderResponse.OrderStatusEnum;
}
export namespace NewOrderResponse {
    export type OrderStatusEnum = 'CREATED' | 'WORKING' | 'PREPARING' | 'DELIVERING' | 'COMPLETED';
    export const OrderStatusEnum = {
        CREATED: 'CREATED' as OrderStatusEnum,
        WORKING: 'WORKING' as OrderStatusEnum,
        PREPARING: 'PREPARING' as OrderStatusEnum,
        DELIVERING: 'DELIVERING' as OrderStatusEnum,
        COMPLETED: 'COMPLETED' as OrderStatusEnum
    };
}