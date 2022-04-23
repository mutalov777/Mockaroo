package uz.mockaroo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import uz.mockaroo.enums.FakerType;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    private String name;
    private FakerType type;
    private Integer size;
    private Integer min;
    private  Integer max;

}

