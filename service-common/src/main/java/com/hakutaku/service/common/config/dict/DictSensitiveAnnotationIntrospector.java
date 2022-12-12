package com.hakutaku.service.common.config.dict;

import com.hakutaku.service.common.annotation.Dict;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import org.springframework.stereotype.Component;

@Component
public class DictSensitiveAnnotationIntrospector extends NopAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    @Override
    public Object findSerializer(Annotated am) {
        Dict dict = am.getAnnotation(Dict.class);
        if (dict != null){
            return DictSerializer.class;
        }
        return null;
    }
}
