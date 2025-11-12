package org.example.defynske.dto.responds;


public record MemberResponse(Long id, String username, String displayName, String role) {
    public static MemberResponse fromEntity(org.example.defynske.model.Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUsername(),
                member.getDisplayName(),
                member.getRole()
        );
    }

}
