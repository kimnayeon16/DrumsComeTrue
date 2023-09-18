package com.example.drumcomestrue.api.response.user;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindIdResponse {

	private String userId;

}
