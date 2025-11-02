package org.example.defynske.dto;


public record MemberResponse(String username, String displayName, String role) {
    public static MemberResponse fromEntity(org.example.defynske.model.Member member) {
        return new MemberResponse(
                member.getUsername(),
                member.getDisplayName(),
                member.getRole()
        );
    }

}
