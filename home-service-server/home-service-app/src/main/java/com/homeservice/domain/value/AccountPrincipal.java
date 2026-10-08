package com.homeservice.domain.value;
import com.homeservice.enums.Role;
import java.time.Instant;
/** accountId 始终来自 auth_account，绝不能充当 worker_profile.id。 */
public record AccountPrincipal(long accountId, Role role, Instant expiresAt) {}
