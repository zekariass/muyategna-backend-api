package com.muyategna.backend.job_request.dto.job_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotBlank(message = "Title is required")
    private String description;

    private BigDecimal budget;

    @NotNull(message = "Start Time Window required")
    private Long startTimeWindowId;

    private List<Map<String, Object>> questionAnswers;

    private Map<String, Object> extraData;

//    public List<Map<String, Object>> parseAndGetQuestionAnswers() {
//        ObjectMapper mapper = new ObjectMapper();
//        // You might need to adjust the TypeReference based on your exact structure
//        try {
//            return mapper.readValue(this.questionAnswers, new TypeReference<List<Map<String, Object>>>() {});
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Map<String, Object> parseAndGetExtraData(){
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.readValue(this.extraData, new TypeReference<Map<String, Object>>(){});
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }


}
