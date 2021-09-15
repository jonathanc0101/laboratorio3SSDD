package bordero.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RestPageImpl<T> extends PageImpl<T>{

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(@JsonProperty("content") List<T> content,
                        @JsonProperty("number") int number,
                        @JsonProperty("size") int size,
                        @JsonProperty("totalElements") Long totalElements,
                        @JsonProperty("pageable") JsonNode pageable,
                        @JsonProperty("last") boolean last,
                        @JsonProperty("totalPages") int totalPages,
                        @JsonProperty("sort") JsonNode sorts,
                        @JsonProperty("first") boolean first,
                        @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size, RestPageImpl.getSorts(sorts)), totalElements);
    }

    public static Sort getSorts(JsonNode jnSorts) {
        Iterator<JsonNode> it = jnSorts.iterator();
        Sort sort = null;
        if (it.hasNext()) {
            JsonNode node = it.next();
            sort = getSort(node);
        }
        while (it.hasNext()) {
            JsonNode another = it.next();
            sort = sort.and(getSort(another));
        }
        if (sort==null) sort = Sort.unsorted();
        return sort;
    }

    public static Sort getSort(JsonNode node) {
        boolean asc = node.get("direction").asText().compareTo("ASC") == 0;
        Sort.Direction dir = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, node.get("property").asText());
    }

    public RestPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public RestPageImpl(List<T> content) {
        super(content);
    }

    public RestPageImpl() {
        super(new ArrayList<>());
    }
}
