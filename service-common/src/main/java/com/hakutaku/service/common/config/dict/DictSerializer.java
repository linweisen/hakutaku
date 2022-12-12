package com.hakutaku.service.common.config.dict;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hakutaku.service.common.annotation.Dict;
import lombok.SneakyThrows;

public class DictSerializer extends StdSerializer<Object> implements ContextualSerializer {

    private static final long serialVersionUID = -6157558261755426448L;

    private String dictCode;

    public DictSerializer() {
        super(Object.class);
    }

    public DictSerializer(String dictCode) {
        super(Object.class);
        this.dictCode = dictCode;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        Dict annotation = property.getAnnotation(Dict.class);
        return new DictSerializer(annotation.value());
    }

    @SneakyThrows
    @Override
    public void serialize(Object code, JsonGenerator gen, SerializerProvider provider) {
//        DictService service = SpringUtils.getBean(DictService.class);
//        SysDictDetail detail = service.getDictMapper(this.dictCode, String.valueOf(code));
//        DictResp resp = new DictResp();
//        resp.setDictCode(detail.getDictCode());
//        resp.setCode(detail.getFieldCode());
//        resp.setValue(detail.getCnName());
//        gen.writeObject(resp);
    }
}
