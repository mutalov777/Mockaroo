package uz.jl.mackaroo.mock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    private String name;
    private TYPE type;
    private Integer size;
    private Integer min;
    private Integer max;

    public enum TYPE {
        FULLNAME,
        FIRST_NAME,
        LAST_NAME,
        USERNAME
    }
}

