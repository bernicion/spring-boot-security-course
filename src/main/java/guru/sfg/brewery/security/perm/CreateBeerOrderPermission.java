package guru.sfg.brewery.security.perm;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('admin.order.create') " +
        " OR hasAuthority('customer.order.create') " +
        " AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
public @interface CreateBeerOrderPermission {
}
