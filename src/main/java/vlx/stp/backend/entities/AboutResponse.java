package vlx.stp.backend.entities;

import lombok.Builder;

@Builder
public record AboutResponse(String version, String branch, String commit, String commitUser, String build_time) {
}
