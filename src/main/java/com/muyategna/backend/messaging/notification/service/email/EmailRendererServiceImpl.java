package com.muyategna.backend.messaging.notification.service.email;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

//
//import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
//import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;
//import freemarker.cache.StringTemplateLoader;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.StringWriter;
//import java.util.Map;
//
//@Service
//@Slf4j
//public class EmailRendererServiceImpl implements EmailRendererService {
//
//
//    @Override
//    public String renderBody(NotificationTemplate notifTemplate, NotificationTemplateTranslation translation, Map<String, Object> payload) {
//        Template template = getBodyTemplate(notifTemplate, translation);
//        try (StringWriter stringWriter = new StringWriter()) {
//            template.process(payload, stringWriter);
//            return stringWriter.toString();
//        } catch (IOException | TemplateException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public String renderSubject(NotificationTemplate notifTemplate, NotificationTemplateTranslation translation, Map<String, Object> payload) {
//        Template template = getSubjectTemplate(notifTemplate, translation);
//        try (StringWriter stringWriter = new StringWriter()) {
//            template.process(payload, stringWriter);
//            return stringWriter.toString();
//        } catch (IOException | TemplateException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Template getBodyTemplate(NotificationTemplate template, NotificationTemplateTranslation translation) {
//        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
//        stringTemplateLoader.putTemplate(template.getName(), translation.getBodyTemplate());
//
//        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
//        cfg.setTemplateLoader(stringTemplateLoader);
//        cfg.setDefaultEncoding("UTF-8");
//
//        try {
//            return cfg.getTemplate(template.getName());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Template getSubjectTemplate(NotificationTemplate template, NotificationTemplateTranslation translation) {
//        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
//        stringTemplateLoader.putTemplate(template.getName(), translation.getSubjectTemplate());
//
//        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
//        cfg.setTemplateLoader(stringTemplateLoader);
//        cfg.setDefaultEncoding("UTF-8");
//
//        try {
//            return cfg.getTemplate(template.getName());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
@Service
@Slf4j
public class EmailRendererServiceImpl implements EmailRendererService {

    private final freemarker.template.Configuration baseConfig;

    @Autowired
    public EmailRendererServiceImpl(freemarker.template.Configuration baseConfig) {
        this.baseConfig = baseConfig;
    }

    private freemarker.template.Configuration cloneWithLoader(StringTemplateLoader loader) {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(baseConfig.getIncompatibleImprovements());
        cfg.setDefaultEncoding(baseConfig.getDefaultEncoding());
        cfg.setTemplateExceptionHandler(baseConfig.getTemplateExceptionHandler());
        cfg.setLogTemplateExceptions(baseConfig.getLogTemplateExceptions());
        cfg.setFallbackOnNullLoopVariable(baseConfig.getFallbackOnNullLoopVariable());
        cfg.setTemplateLoader(loader);
        return cfg;
    }

    private String render(String templateSource, String templateName, Map<String, Object> payload) {
        StringTemplateLoader loader = new StringTemplateLoader();
        loader.putTemplate(templateName, templateSource);

        freemarker.template.Configuration cfg = cloneWithLoader(loader);

        try (StringWriter writer = new StringWriter()) {
            Template template = cfg.getTemplate(templateName);
            template.process(payload, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error("Template rendering failed for {}", templateName, e);
            throw new RuntimeException("Template rendering failed: " + templateName, e);
        }
    }

    @Override
    public String renderBody(NotificationTemplate template, NotificationTemplateTranslation translation, Map<String, Object> payload) {
        return render(translation.getBodyTemplate(), template.getName() + "_body", payload);
    }

    @Override
    public String renderSubject(NotificationTemplate template, NotificationTemplateTranslation translation, Map<String, Object> payload) {
        return render(translation.getSubjectTemplate(), template.getName() + "_subject", payload);
    }

    @Override
    public String renderBody(NotificationTemplate template, String templateText, Map<String, Object> payload) {
        return render(templateText, template + "_body", payload);
    }

    @Override
    public String renderSubject(NotificationTemplate template, String templateText, Map<String, Object> payload) {
        return render(templateText, template + "_subject", payload);
    }
}
