// 由 docs/api/openapi.yaml 生成，禁止手改。
export const routes = {
  "customerLogin": {
    "method": "POST",
    "path": "/customer/auth/login",
    "status": 200,
    "anonymous": true,
    "input": "LoginDTO",
    "output": "LoginVOResponse",
    "query": "",
    "idempotent": false
  },
  "customerCurrentAccount": {
    "method": "GET",
    "path": "/customer/auth/me",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "AccountVOResponse",
    "query": "",
    "idempotent": false
  },
  "workerLogin": {
    "method": "POST",
    "path": "/worker/auth/login",
    "status": 200,
    "anonymous": true,
    "input": "LoginDTO",
    "output": "LoginVOResponse",
    "query": "",
    "idempotent": false
  },
  "workerCurrentAccount": {
    "method": "GET",
    "path": "/worker/auth/me",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "AccountVOResponse",
    "query": "",
    "idempotent": false
  },
  "adminLogin": {
    "method": "POST",
    "path": "/admin/auth/login",
    "status": 200,
    "anonymous": true,
    "input": "LoginDTO",
    "output": "LoginVOResponse",
    "query": "",
    "idempotent": false
  },
  "adminCurrentAccount": {
    "method": "GET",
    "path": "/admin/auth/me",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "AccountVOResponse",
    "query": "",
    "idempotent": false
  },
  "customerRegister": {
    "method": "POST",
    "path": "/customer/auth/register",
    "status": 201,
    "anonymous": true,
    "input": "RegisterDTO",
    "output": "AccountVOResponse",
    "query": "",
    "idempotent": true
  },
  "listServiceRegions": {
    "method": "GET",
    "path": "/customer/regions",
    "status": 200,
    "anonymous": true,
    "input": "",
    "output": "RegionListResponse",
    "query": "",
    "idempotent": false
  },
  "getBookingRules": {
    "method": "GET",
    "path": "/customer/booking-rules",
    "status": 200,
    "anonymous": true,
    "input": "",
    "output": "BookingRulesVOResponse",
    "query": "",
    "idempotent": false
  },
  "listCustomerCategory": {
    "method": "GET",
    "path": "/customer/categories",
    "status": 200,
    "anonymous": true,
    "input": "",
    "output": "CategoryPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "listCustomerServiceItem": {
    "method": "GET",
    "path": "/customer/service-items",
    "status": 200,
    "anonymous": true,
    "input": "",
    "output": "ServiceItemPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "listCustomerSku": {
    "method": "GET",
    "path": "/customer/skus",
    "status": 200,
    "anonymous": true,
    "input": "",
    "output": "SkuPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "getCustomerSku": {
    "method": "GET",
    "path": "/customer/skus/{id}",
    "status": 200,
    "anonymous": true,
    "input": "",
    "output": "SkuVOResponse",
    "query": "",
    "idempotent": false
  },
  "listAddresses": {
    "method": "GET",
    "path": "/customer/addresses",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "AddressListResponse",
    "query": "",
    "idempotent": false
  },
  "createAddress": {
    "method": "POST",
    "path": "/customer/addresses",
    "status": 201,
    "anonymous": false,
    "input": "AddressDTO",
    "output": "AddressVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateAddress": {
    "method": "PUT",
    "path": "/customer/addresses/{id}",
    "status": 200,
    "anonymous": false,
    "input": "AddressDTO",
    "output": "AddressVOResponse",
    "query": "",
    "idempotent": true
  },
  "deleteAddress": {
    "method": "DELETE",
    "path": "/customer/addresses/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "MutationVOResponse",
    "query": "",
    "idempotent": true
  },
  "createOrder": {
    "method": "POST",
    "path": "/customer/orders",
    "status": 201,
    "anonymous": false,
    "input": "CreateOrderDTO",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "customerListOrders": {
    "method": "GET",
    "path": "/customer/orders",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderPageDTOResponse",
    "query": "OrderQuery",
    "idempotent": false
  },
  "customerGetOrder": {
    "method": "GET",
    "path": "/customer/orders/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": false
  },
  "customerGetOrderHistory": {
    "method": "GET",
    "path": "/customer/orders/{id}/history",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderHistoryVOResponse",
    "query": "",
    "idempotent": false
  },
  "workerListOrders": {
    "method": "GET",
    "path": "/worker/orders",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderPageDTOResponse",
    "query": "OrderQuery",
    "idempotent": false
  },
  "workerGetOrder": {
    "method": "GET",
    "path": "/worker/orders/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": false
  },
  "workerGetOrderHistory": {
    "method": "GET",
    "path": "/worker/orders/{id}/history",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderHistoryVOResponse",
    "query": "",
    "idempotent": false
  },
  "adminListOrders": {
    "method": "GET",
    "path": "/admin/orders",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderPageDTOResponse",
    "query": "OrderQuery",
    "idempotent": false
  },
  "adminGetOrder": {
    "method": "GET",
    "path": "/admin/orders/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": false
  },
  "adminGetOrderHistory": {
    "method": "GET",
    "path": "/admin/orders/{id}/history",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderHistoryVOResponse",
    "query": "",
    "idempotent": false
  },
  "payOrder": {
    "method": "POST",
    "path": "/customer/orders/{id}/payments",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "changeOffer": {
    "method": "PUT",
    "path": "/customer/orders/{id}/offer",
    "status": 200,
    "anonymous": false,
    "input": "ChangeOfferDTO",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "cancelCustomerOrder": {
    "method": "POST",
    "path": "/customer/orders/{id}/cancellations",
    "status": 200,
    "anonymous": false,
    "input": "CancelOrderDTO",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "getStartCode": {
    "method": "GET",
    "path": "/customer/orders/{id}/start-code",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "StartCodeVOResponse",
    "query": "",
    "idempotent": false
  },
  "confirmOrder": {
    "method": "POST",
    "path": "/customer/orders/{id}/confirmations",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "createReview": {
    "method": "POST",
    "path": "/customer/orders/{id}/reviews",
    "status": 201,
    "anonymous": false,
    "input": "ReviewDTO",
    "output": "ReviewVOResponse",
    "query": "",
    "idempotent": true
  },
  "getWorkerProfile": {
    "method": "GET",
    "path": "/worker/profile",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "WorkerVOResponse",
    "query": "",
    "idempotent": false
  },
  "getSchedule": {
    "method": "GET",
    "path": "/worker/schedule",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "ScheduleVOResponse",
    "query": "",
    "idempotent": false
  },
  "updateSchedule": {
    "method": "PUT",
    "path": "/worker/schedule",
    "status": 200,
    "anonymous": false,
    "input": "ScheduleDTO",
    "output": "ScheduleVOResponse",
    "query": "",
    "idempotent": true
  },
  "listWorkerSlots": {
    "method": "GET",
    "path": "/worker/slots",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "SlotListResponse",
    "query": "SlotQuery",
    "idempotent": false
  },
  "listLeaves": {
    "method": "GET",
    "path": "/worker/leaves",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "LeavePageDTOResponse",
    "query": "PageQuery",
    "idempotent": false
  },
  "createLeave": {
    "method": "POST",
    "path": "/worker/leaves",
    "status": 201,
    "anonymous": false,
    "input": "LeaveDTO",
    "output": "LeaveVOResponse",
    "query": "",
    "idempotent": true
  },
  "cancelLeave": {
    "method": "DELETE",
    "path": "/worker/leaves/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "MutationVOResponse",
    "query": "",
    "idempotent": true
  },
  "listEligibleOffers": {
    "method": "GET",
    "path": "/worker/offers",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OfferPageDTOResponse",
    "query": "OfferQuery",
    "idempotent": false
  },
  "claimOffer": {
    "method": "POST",
    "path": "/worker/offers/{id}/claims",
    "status": 200,
    "anonymous": false,
    "input": "ClaimOfferDTO",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "departOrder": {
    "method": "POST",
    "path": "/worker/orders/{id}/departures",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "arriveOrder": {
    "method": "POST",
    "path": "/worker/orders/{id}/arrivals",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "startOrder": {
    "method": "POST",
    "path": "/worker/orders/{id}/starts",
    "status": 200,
    "anonymous": false,
    "input": "StartServiceDTO",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "finishOrder": {
    "method": "POST",
    "path": "/worker/orders/{id}/completions",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "listAdminCategory": {
    "method": "GET",
    "path": "/admin/categories",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "CategoryPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "createAdminCategory": {
    "method": "POST",
    "path": "/admin/categories",
    "status": 201,
    "anonymous": false,
    "input": "CategoryDTO",
    "output": "CategoryVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateAdminCategory": {
    "method": "PUT",
    "path": "/admin/categories/{id}",
    "status": 200,
    "anonymous": false,
    "input": "CategoryDTO",
    "output": "CategoryVOResponse",
    "query": "",
    "idempotent": true
  },
  "deleteAdminCategory": {
    "method": "DELETE",
    "path": "/admin/categories/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "MutationVOResponse",
    "query": "",
    "idempotent": true
  },
  "listAdminServiceItem": {
    "method": "GET",
    "path": "/admin/service-items",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "ServiceItemPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "createAdminServiceItem": {
    "method": "POST",
    "path": "/admin/service-items",
    "status": 201,
    "anonymous": false,
    "input": "ServiceItemDTO",
    "output": "ServiceItemVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateAdminServiceItem": {
    "method": "PUT",
    "path": "/admin/service-items/{id}",
    "status": 200,
    "anonymous": false,
    "input": "ServiceItemDTO",
    "output": "ServiceItemVOResponse",
    "query": "",
    "idempotent": true
  },
  "deleteAdminServiceItem": {
    "method": "DELETE",
    "path": "/admin/service-items/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "MutationVOResponse",
    "query": "",
    "idempotent": true
  },
  "listAdminSku": {
    "method": "GET",
    "path": "/admin/skus",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "SkuPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "createAdminSku": {
    "method": "POST",
    "path": "/admin/skus",
    "status": 201,
    "anonymous": false,
    "input": "SkuDTO",
    "output": "SkuVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateAdminSku": {
    "method": "PUT",
    "path": "/admin/skus/{id}",
    "status": 200,
    "anonymous": false,
    "input": "SkuDTO",
    "output": "SkuVOResponse",
    "query": "",
    "idempotent": true
  },
  "deleteAdminSku": {
    "method": "DELETE",
    "path": "/admin/skus/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "MutationVOResponse",
    "query": "",
    "idempotent": true
  },
  "listAdminSkill": {
    "method": "GET",
    "path": "/admin/skills",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "SkillPageDTOResponse",
    "query": "CatalogQuery",
    "idempotent": false
  },
  "createAdminSkill": {
    "method": "POST",
    "path": "/admin/skills",
    "status": 201,
    "anonymous": false,
    "input": "SkillDTO",
    "output": "SkillVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateAdminSkill": {
    "method": "PUT",
    "path": "/admin/skills/{id}",
    "status": 200,
    "anonymous": false,
    "input": "SkillDTO",
    "output": "SkillVOResponse",
    "query": "",
    "idempotent": true
  },
  "deleteAdminSkill": {
    "method": "DELETE",
    "path": "/admin/skills/{id}",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "MutationVOResponse",
    "query": "",
    "idempotent": true
  },
  "listAccounts": {
    "method": "GET",
    "path": "/admin/accounts",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "AccountPageDTOResponse",
    "query": "AccountQuery",
    "idempotent": false
  },
  "setAccountStatus": {
    "method": "PUT",
    "path": "/admin/accounts/{id}/status",
    "status": 200,
    "anonymous": false,
    "input": "AccountStatusDTO",
    "output": "AccountVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateAccountProfile": {
    "method": "PUT",
    "path": "/admin/accounts/{id}/profile",
    "status": 200,
    "anonymous": false,
    "input": "AccountProfileDTO",
    "output": "AccountVOResponse",
    "query": "",
    "idempotent": true
  },
  "listWorkers": {
    "method": "GET",
    "path": "/admin/workers",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "WorkerPageDTOResponse",
    "query": "WorkerQuery",
    "idempotent": false
  },
  "createWorker": {
    "method": "POST",
    "path": "/admin/workers",
    "status": 201,
    "anonymous": false,
    "input": "WorkerCreateDTO",
    "output": "WorkerVOResponse",
    "query": "",
    "idempotent": true
  },
  "updateWorker": {
    "method": "PUT",
    "path": "/admin/workers/{id}",
    "status": 200,
    "anonymous": false,
    "input": "WorkerDTO",
    "output": "WorkerVOResponse",
    "query": "",
    "idempotent": true
  },
  "getAdminWorkerSlots": {
    "method": "GET",
    "path": "/admin/workers/{id}/slots",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "SlotListResponse",
    "query": "SlotQuery",
    "idempotent": false
  },
  "cancelAdminOrder": {
    "method": "POST",
    "path": "/admin/orders/{id}/cancellations",
    "status": 200,
    "anonymous": false,
    "input": "CancelOrderDTO",
    "output": "OrderVOResponse",
    "query": "",
    "idempotent": true
  },
  "listDispatchAttempts": {
    "method": "GET",
    "path": "/admin/dispatch-attempts",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "DispatchAttemptPageDTOResponse",
    "query": "RecordQuery",
    "idempotent": false
  },
  "listPayments": {
    "method": "GET",
    "path": "/admin/payments",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "PaymentPageDTOResponse",
    "query": "RecordQuery",
    "idempotent": false
  },
  "listAudits": {
    "method": "GET",
    "path": "/admin/audits",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "AuditPageDTOResponse",
    "query": "RecordQuery",
    "idempotent": false
  },
  "getSettings": {
    "method": "GET",
    "path": "/admin/settings",
    "status": 200,
    "anonymous": false,
    "input": "",
    "output": "BookingRulesVOResponse",
    "query": "",
    "idempotent": false
  },
  "updateSettings": {
    "method": "PUT",
    "path": "/admin/settings",
    "status": 200,
    "anonymous": false,
    "input": "SettingsDTO",
    "output": "BookingRulesVOResponse",
    "query": "",
    "idempotent": true
  }
} as const
