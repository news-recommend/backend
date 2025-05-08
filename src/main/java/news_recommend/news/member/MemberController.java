package news_recommend.news.member;

import news_recommend.news.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService){
        this.memberService = memberService;
    }


//    @GetMapping
//    public ResponseEntity<List<Member>> getMembers() {
//        return ResponseEntity.ok(memberService.findMembers());
//    }
//
//    @PostMapping
//    public ResponseEntity<Member> createMember(@RequestBody Member member) {
//        return ResponseEntity.ok(memberService.save(member));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Member> getMember(@PathVariable Long id) {
//        return memberService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
//        updatedMember.setUserId(id);
//        memberService.update(updatedMember);
//        return ResponseEntity.ok(updatedMember);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
//        memberService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
    // 이 위에거는 그냥 다 참고용으로만 쓰는게 좋을듯?

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Member>> getMyProfile() {
        try {
            Member member = memberService.findMyProfile();
            return ResponseEntity.ok(ApiResponse.success(member));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("프로필을 찾을 수 없습니다.", "not_found"));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateMyProfile(@RequestBody MemberUpdateRequest request) {
        try {
            memberService.updateMyProfile(request);
            Map<String, String> result = new HashMap<>();
            result.put("message", "변경 성공");
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IllegalArgumentException e) {
            // 비밀번호 오류
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), "password"));
        } catch (Exception e) {
            // 기타 오류
            return ResponseEntity.badRequest().body(ApiResponse.error("입력 형식이 올바르지 않습니다.", "type"));
        }
    }


}
