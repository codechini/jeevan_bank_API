# Jeevan Bank API - Implementation TODO List

## ✅ COMPLETED

### Auth Endpoints
| # | Endpoint | Status |
|---|----------|--------|
| 1 | `POST /api/auth/login` | ✅ Done |
| 2 | `POST /api/auth/register` | ✅ Done |

### User Endpoints
| # | Endpoint | Status |
|---|----------|--------|
| 3 | `POST /api/user/openaccount` | ✅ Done |
| 4 | `GET /api/user/accounts` | ✅ Done |
| 5 | `GET /api/user/accounts/{id}` | ✅ Done |
| 6 | `PUT /api/user/accounts/{id}` | ✅ Done |
| 7 | `PUT /api/user/accounts/{id}/close` | ✅ Done |
| 8 | `DELETE /api/user/accounts/{id}` | ✅ Done |
| 9 | `POST /api/user/accounts/{id}/deposit` | ✅ Done |
| 10 | `POST /api/user/accounts/{id}/withdraw` | ✅ Done |
| 11 | `POST /api/user/accounts/{id}/transfer` | ✅ Done |
| 12 | `GET /api/user/accounts/{id}/transactions` | ✅ Done |
| 13 | `GET /api/user/accounts/{id}/transactions/paginated` | ✅ Done |
| 14 | `POST /api/user/applyloan` | ✅ Done |
| 15 | `GET /api/user/viewloan` | ✅ Done |
| 16 | `GET /api/user/viewchequebook` | ✅ Done |
| 17 | `POST /api/user/applychequebook` | ✅ Done |

### Admin Endpoints
| # | Endpoint | Status |
|---|----------|--------|
| 18 | `POST /api/admin/accounts/open` | ✅ Done |
| 19 | `GET /api/admin/accounts` | ✅ Done |
| 20 | `GET /api/admin/accounts/{id}` | ✅ Done |
| 21 | `PUT /api/admin/accounts/{id}` | ✅ Done |
| 22 | `PUT /api/admin/accounts/{id}/close` | ✅ Done |
| 23 | `DELETE /api/admin/accounts/{id}` | ✅ Done |
| 24 | `GET /api/admin/transactions` | ✅ Done |
| 25 | `GET /api/admin/users` | ✅ Done |
| 26 | `GET /api/admin/users/{id}` | ✅ Done |
| 27 | `PUT /api/admin/users/{id}/activate` | ✅ Done |
| 28 | `PUT /api/admin/users/{id}/deactivate` | ✅ Done |
| 29 | `PUT /api/admin/users/{id}/role` | ✅ Done |
| 30 | `POST /api/admin/accounts/{id}/deposit` | ✅ Done |
| 31 | `POST /api/admin/accounts/{id}/withdraw` | ✅ Done |
| 32 | `POST /api/admin/accounts/{id}/transfer` | ✅ Done |
| 33 | `GET /api/admin/loans` | ✅ Done |
| 34 | `POST /api/admin/loans/{id}/approve` | ✅ Done |
| 35 | `POST /api/admin/loans/{id}/reject` | ✅ Done |
| 36 | `GET /api/admin/chequebooks` | ✅ Done |
| 37 | `POST /api/admin/chequebooks/{id}/approve` | ✅ Done |
| 38 | `POST /api/admin/chequebooks/{id}/reject` | ✅ Done |

---

## ❌ NOT YET IMPLEMENTED

### User Endpoints
| # | Endpoint | Status |
|---|----------|--------|
| 1 | `POST /api/user/applycard` | ✅ Done |
| 2 | `GET /api/user/viewcard` | ✅ Done |

### Admin Endpoints
| # | Endpoint | Status |
|---|----------|--------|
| 1 | `GET /api/admin/cards` | ✅ Done |
| 2 | `POST /api/admin/cards/{id}/approve` | ✅ Done |
| 3 | `POST /api/admin/cards/{id}/reject` | ✅ Done |

---

## Priority Order for Implementation

1. **Admin Transaction APIs** (deposit/withdraw/transfer) ✅ DONE
2. **Loan APIs** ✅ DONE
3. **Chequebook APIs** ✅ DONE
4. **Card APIs** - requires creating Card controller/service
