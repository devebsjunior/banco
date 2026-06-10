package br.com.arq.dto.response;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString(exclude={"token"})
public class ResponseLoginDTO {

    private Map<String,Object> tokensMap = new HashMap<>();
    private String token;

    public ResponseLoginDTO(String token){
        this.token = token;
    }

}
