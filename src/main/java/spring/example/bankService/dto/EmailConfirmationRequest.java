package spring.example.bankService.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailConfirmationRequest {

    private  String email;
    private  String confirmationCode;
}
