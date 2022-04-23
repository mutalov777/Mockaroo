package uz.mockaroo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.mockaroo.enums.FileFormat;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    private List<Field> field;

    private int count;

    private FileFormat format;

}
