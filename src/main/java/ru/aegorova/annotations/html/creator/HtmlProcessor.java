package ru.aegorova.annotations.html.creator;

import com.google.auto.service.AutoService;
import freemarker.template.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("ru.aegorova.annotations.html.creator.HtmlForm")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class HtmlProcessor extends AbstractProcessor {


    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // get types with "HtmlForm" annotation
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(HtmlForm.class);
        // get path with class-file
        String directPath = HtmlProcessor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        directPath = File.separator + directPath;
        for (Element element : annotatedElements) {
            Map<String, Object> parametersMap = new HashMap<>();
            // create path to html-file
            // User.class -> User.html
            String resultPath = directPath.substring(1) + element.getSimpleName().toString().toLowerCase() + ".html";
            Path out = Paths.get(resultPath);
            try {
                // create freemarker configuration
                Configuration configuration = configureFreemarker(directPath);
                StringWriter result = new StringWriter();
                BufferedWriter writer = new BufferedWriter(new FileWriter(out.toFile()));
                // put into map html-form information
                HtmlForm htmlFormAnnotation = element.getAnnotation(HtmlForm.class);
                parametersMap.put("actions", htmlFormAnnotation.action());
                parametersMap.put("method", htmlFormAnnotation.method());
                // put into map html-input information
                List<? extends Element> elements = element.getEnclosedElements();
                parametersMap.putAll(htmlInputMap(elements));
                // replace keys in template
                Template template = configuration.getTemplate(element.getSimpleName().toString().toLowerCase() + ".ftl");
                template.process(parametersMap, result);
                // write into .html file
                writer.write(result.toString());
                writer.close();
            } catch (IOException | TemplateException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return true;
    }

    private Configuration configureFreemarker(String path) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_30);
        owb.setIterableSupport(true);
        cfg.setObjectWrapper(owb.build());
        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        return cfg;
    }

    private Map<String, Object> htmlInputMap(List<? extends Element> elements) {
        return new HashMap<String, Object>() {{
            put("inputs", elements.stream().filter(element -> element.getAnnotation(HtmlInput.class) != null)
                    .map(element -> new Input(element.getAnnotation(HtmlInput.class).type(),
                        element.getAnnotation(HtmlInput.class).name(),
                        element.getAnnotation(HtmlInput.class).placeholder()))
                    .collect(Collectors.toList()));
        }};

    }

}
