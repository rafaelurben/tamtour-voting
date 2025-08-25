package ch.rafaelurben.tamtour.voting.controller.admin;

import ch.rafaelurben.tamtour.voting.dto.admin.*;
import ch.rafaelurben.tamtour.voting.security.UserRoles;
import ch.rafaelurben.tamtour.voting.service.admin.AdminViewerKeyService;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/viewer/keys")
@RolesAllowed(UserRoles.ADMIN)
@RequiredArgsConstructor
public class AdminViewerKeysController {
  private final AdminViewerKeyService adminViewerKeyService;

  @GetMapping("")
  public List<ResultViewerKeyDto> getKeys() {
    return adminViewerKeyService.getAllKeys();
  }

  @PostMapping("")
  public ResultViewerKeyDto createKey(@RequestBody ResultViewerKeyRequestDto dto) {
    return adminViewerKeyService.createKey(dto);
  }

  @PutMapping("/{id}")
  public ResultViewerKeyDto updateKey(
      @PathVariable Long id, @RequestBody ResultViewerKeyRequestDto updateDto) {
    return adminViewerKeyService.updateKey(id, updateDto);
  }

  @DeleteMapping("/{id}")
  public void deleteKey(@PathVariable Long id) {
    adminViewerKeyService.deleteKey(id);
  }
}
