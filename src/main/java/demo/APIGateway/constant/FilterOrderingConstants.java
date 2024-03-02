package demo.APIGateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterOrderingConstants {

    public final static int AUTHORIZATION_FILTER_ORDER = 0;
    public final static int FALLBACK_FILTER_ORDER = -1;
}