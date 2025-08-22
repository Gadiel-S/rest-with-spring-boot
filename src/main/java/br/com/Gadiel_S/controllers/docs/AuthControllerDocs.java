package br.com.Gadiel_S.controllers.docs;

import br.com.Gadiel_S.data.dto.security.AccountCredentialsDTO;
import br.com.Gadiel_S.data.dto.security.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {
  @Operation(summary = "Authenticate user",
      description = "Authenticates an user and returns an access token and refresh token",
      tags = {"Authentication Endpoint!"},
      responses = {
          @ApiResponse(
            description = "Success",
            responseCode = "200",
            content = {
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TokenDTO.class)
                )
            }),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    }
  )
  ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentials);

  @Operation(summary = "Refresh token",
      description = "Refresh token for authenticated user and returns a new access token",
      tags = {"Authentication Endpoint!"},
      responses = {
          @ApiResponse(
            description = "Success",
            responseCode = "200",
            content = {
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TokenDTO.class)
                )
            }),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      }
  )
  ResponseEntity<?> refreshToken(
      @PathVariable("username") String username,
      @RequestHeader("Authorization") String refreshToken
  );

  @Operation(summary = "Create user (dev only)",
      description = "Creates a user — dev-only endpoint",
      tags = {"Authentication Endpoint!"},
      responses = {
          @ApiResponse(
            description = "Created",
            responseCode = "201",
            content = @Content(schema = @Schema(implementation = AccountCredentialsDTO.class))
          ),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      }
  )
  AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials);
}
