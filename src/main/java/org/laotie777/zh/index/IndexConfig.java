package org.laotie777.zh.index;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @Author yuh
 * @Date Created in 下午1:01 2018/2/9
 * @Description 包装多个indexBean
 */
public class IndexConfig {
    /**
     * 包装多个配置实体
     */
    private Set<IndexBean> config;

    /**
     * 用时才加载的内部类 默认的写法
     */
    static class LoayLoadIndexConfig{
        private static Set<IndexBean> defaultConfig = null;
        static {
            defaultConfig = new HashSet<>();
            defaultConfig.add(new IndexBean());
        }
    }

    public Set<IndexBean> getConfig() {
        if(Objects.isNull(config)){
            config = LoayLoadIndexConfig.defaultConfig;
        }
        return config;
    }

    public void setConfig(Set<IndexBean> config) {
        this.config = config;
    }
}
