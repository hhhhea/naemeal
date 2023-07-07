package mega.naemeal.profile.service;

import mega.naemeal.profile.dto.request.ProfileRequestDto;
import mega.naemeal.profile.dto.response.ProfileResponseDto;

public interface ProfileService {

  ProfileResponseDto getCustomerProfile(String userId);
  String getProfileImage(String userId);
  ProfileResponseDto updateProfile(String userId, ProfileRequestDto requestDto, String imgPath);
}
