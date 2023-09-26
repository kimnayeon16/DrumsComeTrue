// package com.example.drumcomestrue;
//
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
// import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
// import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
// import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
// @EnableMockMvc
// @AutoConfigureRestDocs
// public class ApiDocument {
//
// 	@Autowired
// 	protected MockMvc mockMvc;
//
// 	@Autowired
// 	private ObjectMapper objectMapper;
//
// 	protected void printAndMakeSnippet(ResultActions resultActions, String title) throws Exception {
// 		resultActions.andDo(print())
// 			.andDo(toDocument(title));
// 	}
//
// 	protected RestDocumentationResultHandler toDocument(String title) {
// 		return document(title, getDocumentRequest(), getDocumentResponse());
// 	}
//
// 	protected String toJson(Object object) {
// 		try {
// 			objectMapper.registerModule(new JavaTimeModule());
// 			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
// 			return objectMapper.writeValueAsString(object);
// 		} catch (JsonProcessingException e) {
// 			throw new IllegalStateException("직렬화 오류");
// 		}
// 	}
//
// 	private OperationRequestPreprocessor getDocumentRequest() {
// 		return preprocessRequest(
// 			modifyUris()
// 				.scheme("http")
// 				.host("localhost")
// 				.removePort(),
// 			prettyPrint());
// 	}
//
// 	private OperationResponsePreprocessor getDocumentResponse() {
// 		return preprocessResponse(prettyPrint());
// 	}
// }