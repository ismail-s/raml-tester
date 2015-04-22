package guru.nidi.ramltester.core;

import org.raml.model.*;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;

import java.util.*;

/**
 *
 */
class SecurityExtractor {
    private final List<SecurityScheme> schemes;

    public SecurityExtractor(Raml raml, Action action) {
        schemes = securedBy(raml, action);
    }

    private class RemovePropagatingList<T> extends ArrayList<T> {
        @Override
        public T remove(int index) {
            final T removed = super.remove(index);
            schemes.remove(index);
            return removed;
        }
    }

    public List<Map<String, QueryParameter>> queryParameters() {
        final List<Map<String, QueryParameter>> res = new RemovePropagatingList<>();
        for (SecurityScheme scheme : schemes) {
            res.add(scheme.getDescribedBy().getQueryParameters());
        }
        return res;
    }

    public List<Map<String, Header>> headers() {
        final List<Map<String, Header>> res = new RemovePropagatingList<>();
        for (SecurityScheme scheme : schemes) {
            res.add(scheme.getDescribedBy().getHeaders());
        }
        return res;
    }

    public List<Map<String, Response>> responses() {
        final List<Map<String, Response>> res = new RemovePropagatingList<>();
        for (SecurityScheme scheme : schemes) {
            res.add(scheme.getDescribedBy().getResponses());
        }
        return res;
    }

    private List<SecurityScheme> securedBy(Raml raml, Action action) {
        final List<SecurityScheme> res = new ArrayList<>();
        final Set<String> names = new HashSet<>();
        res.addAll(securitySchemes(raml, action.getSecuredBy(), names));
        res.addAll(securitySchemes(raml, action.getResource().getSecuredBy(), names));
        res.addAll(securitySchemes(raml, raml.getSecuredBy(), names));
        return res;
    }

    private List<SecurityScheme> securitySchemes(Raml raml, List<SecurityReference> refs, Set<String> ignore) {
        final List<SecurityScheme> res = new ArrayList<>();
        for (SecurityReference ref : refs) {
            final String name = ref.getName();
            if (!ignore.contains(name)) {
                ignore.add(name);
                res.add(securityScheme(raml, name));
            }
        }
        return res;
    }

    private SecurityScheme securityScheme(Raml raml, String name) {
        for (final Map<String, SecurityScheme> map : raml.getSecuritySchemes()) {
            if (map.containsKey(name)) {
                return map.get(name);
            }
        }
        return null;
    }

}
