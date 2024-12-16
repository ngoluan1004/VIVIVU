package com.example.jpa_relationn.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.jpa_relationn.dto.request.AuthenticationRequest;
import com.example.jpa_relationn.dto.request.IntrospectRequest;
import com.example.jpa_relationn.dto.response.AuthenticationResponse;
import com.example.jpa_relationn.dto.response.IntrospectResponse;
import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal // đánh dấu để nó không inject vì đây là chữ ký bí mật
    @Value("${jwt.signerKey}") // cái này đọc biến từ file application
    protected String SIGNER_KEY;

    // hàm này để validate jwt token
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        // khai báo 1 biến để lưu trữ token lấy từ request
        var token = request.getToken();

        // thư viện nimbus cung cấp cho JWSVerifier để xác thực key(chữ kí)
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // SignedJWT phân tích cú pháp chuỗi token thành đối tượng signedJWT để truy
        // xuất claim và header
        SignedJWT signedJWT = SignedJWT.parse(token);

        // kiểm tra đối tượng signedJWT đã hết hạn hay chưa
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // xác thực signedJWT // trả về true(thành công) false(ko thành công)
        var verified = signedJWT.verify(verifier);

        // thời gian hết hạn phải sau cái thời điểm hiện tại "after(new Date())""
        return IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }

    // hàm này để Authenticate(Xác thực) người dùng dựa trên username|password được
    // truyền vào sau đó nếu thành công thì generate ra 1 jwt token nếu thất bại thì
    // ném ra lỗi
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // khai báo 1 trình mã hóa mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword()); // xác định xem user có đăng nhập thành công không

        if (!authenticated)
            // nếu đăng nhập không thành công thì báo lỗi
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // nếu đăng nhập thành công thì generate ra 1 cái token
        var token = generateToken(user);

        // trả về mã trạng thái và token
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public String generateToken(User user) {
        // khai báo header để truyền vào trong jwsObject
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // để tạo đc 1 payload ta cần biết về claims | data trong body(nội dung sẽ gửi
        // đi) được gọi là claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // claims.subject đại diện cho cái user đăng nhập
                .issuer("example.com") // để xác định token được issue từ ai
                .issueTime(new Date()) // lấy thời điểm hiện tại
                .expirationTime(new Date( // xét thời gian hết hạn trong vòng 1 giờ
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // xác định thời hạn của nó
                .claim("scope", buildScope(user)) // thêm 1 claim tùy chỉnh
                .build();

        // sau khi đủ claimSet ta tạo payload // truyền jwtClaimsSet vào // sau đó
        // convert claimSet về jsonobject
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // để tạo token bằng nimbus ta cần JWSObject
        JWSObject jwsObject = new JWSObject(header, payload);

        // sau khi tạo token xong ta cần kí và để kí token ta dùng "sign()" method
        // truyền một "MACSigner" mới với đối số là "SIGNER_KEY.getBytes()"
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // trả về jwsObject.serialize() là một chuỗi
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    // private String buildScope(User user) {
    //     StringJoiner stringJoiner = new StringJoiner(" ");
    //     if(!CollectionUtils.isEmpty(user.getRoles()))
    //         user.getRoles().forEach(stringJoiner::add);
        
    //         return stringJoiner.toString();
    // }
    private String buildScope(User user) {
        if (user.getRole() != null) {
            // Chuyển enum thành chuỗi bằng phương thức toString() hoặc name()
            return user.getRole().toString(); // hoặc user.getRole().name();
        }
        return "";
    }
}

// => sau khi đã issuer ra cái token rồi thì ta cần phải verify cái token đó ,
// để xem nó có hợp lệ hay không và nó có đúng là của hệ thống issuer ra không
