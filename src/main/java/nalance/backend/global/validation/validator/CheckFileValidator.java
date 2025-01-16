package nalance.backend.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nalance.backend.global.error.code.status.ErrorStatus;
import nalance.backend.global.validation.annotation.CheckFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CheckFileValidator implements ConstraintValidator<CheckFile, MultipartFile> {

    @Override
    public void initialize(CheckFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.NOT_UPLOAD_FILE.toString())
                    .addConstraintViolation();
            return false;
        }

        long maxSize = 5 * 1024 * 1024; // 5MB
        if (value.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.TOO_BIG_SIZE_FILE.toString())
                    .addConstraintViolation();
            return false;
        }

        String extension = getFileExtension(value.getOriginalFilename());
        if (!isValidExtension(extension)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INVALID_FILE_EXTENSION.toString())
                    .addConstraintViolation();
            return false;
        }

        String mimeType = value.getContentType();
        if (!isValidMimeType(mimeType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INVALID_MIME_TYPE.toString())
                    .addConstraintViolation();
            return false;
        }

        String fileName = value.getOriginalFilename();
        if (fileName.contains("..")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INVALID_FILE_NAME.toString())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    // 파일 확장자 유효성 확인
    private boolean isValidExtension(String extension) {
        String[] allowedExtensions = {"jpg", "jpeg", "png"};
        for (String allowedExtension : allowedExtensions) {
            if (allowedExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    // MIME 타입 유효성 확인
    private boolean isValidMimeType(String mimeType) {
        return mimeType != null && (mimeType.equalsIgnoreCase("image/jpeg") || mimeType.equalsIgnoreCase("image/png"));
    }

    // 파일 확장자 추출
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
