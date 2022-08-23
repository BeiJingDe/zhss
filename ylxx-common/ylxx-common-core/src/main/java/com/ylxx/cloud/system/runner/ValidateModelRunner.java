package com.ylxx.cloud.system.runner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylxx.cloud.core.annotation.CustomValidate;
import com.ylxx.cloud.core.annotation.ValidateModel;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.runner.handler.AnnotationRuleHandler;
import com.ylxx.cloud.system.validate.util.ValidateModelCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class ValidateModelRunner implements ApplicationRunner {

    @Value("#{'${my.basePackages:com.ylxx}'.split(',')}")
    private List<String> basePackages;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info(StrUtil.format("扫描包{}生成前后端一致校验model", JSON.toJSONString(basePackages)));
            log.info("将前后端一致校验model从redis中删除");
			ValidateModelCacheUtil.deleteCache(appName);
            log.info("将前后端一致校验model缓存到redis中#start");
            for (String packageName : basePackages) {
                Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(packageName, ValidateModel.class);
                for (Class<?> clazz : classes) {
                    // 获取所有父类
                    Class<?> temp = clazz;
                    List<Class<?>> models = CollUtil.newArrayList();
                    while (!StrUtil.equals(temp.getName(), Object.class.getName())) {
                        models.add(temp);
                        temp = temp.getSuperclass();
                    }
                    models = CollUtil.reverse(models);
                    // 获取每个属性上的校验规则
                    JSONObject modelRule = new JSONObject();
                    for (Class<?> model : models) {
                        model = Class.forName(model.getName());
                        Field[] fields = model.getDeclaredFields();
                        for (Field field : fields) {
                            if (!Modifier.isStatic(field.getModifiers())) {
                                Annotation[] annotations = field.getAnnotations();
                                JSONArray propRules = new JSONArray();
                                for (Annotation annotation : annotations) {
                                    JSONArray subRules = this.genSubRules(annotation);
                                    propRules.addAll(subRules);
                                }
                                if (ObjectUtil.isNotEmpty(propRules)) {
                                    modelRule.put(field.getName(), propRules);
                                }
                            }
                        }
                    }
					ValidateModelCacheUtil.setModelRule(appName, clazz.getSimpleName(), modelRule.toString());
                    log.info(StrUtil.format("appName={},\tmodelName={},\tmodelRules={}", appName, clazz.getSimpleName(), modelRule.toString()));
                }
            }
            log.info("将前后端一致校验model缓存到redis中#end");
        } catch (Exception e) {
            throw new ServiceException("刷新前后端一致校验model缓存失败", e);
        }
    }

    /**
     * @param annotation
     * @return
     * @Title: genSubRules
     * @Description: 生成属性的校验规则
     */
    private JSONArray genSubRules(Annotation annotation) {
        if (annotation instanceof CustomValidate) {
            return AnnotationRuleHandler.generate((CustomValidate) annotation);
        }
        if (annotation instanceof NotNull) {
            return AnnotationRuleHandler.generate((NotNull) annotation);
        }
        if (annotation instanceof NotEmpty) {
            return AnnotationRuleHandler.generate((NotEmpty) annotation);
        }
        if (annotation instanceof NotBlank) {
            return AnnotationRuleHandler.generate((NotBlank) annotation);
        }
        if (annotation instanceof Size) {
            return AnnotationRuleHandler.generate((Size) annotation);
        }
        if (annotation instanceof Range) {
            return AnnotationRuleHandler.generate((Range) annotation);
        }
        return new JSONArray();
    }

}
