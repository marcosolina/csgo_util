package com.ixigo.library.messages;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Implementation of {@link IxigoMessageResource} based on the SpringBoot {@link ReloadableResourceBundleMessageSource}
 * 
 * @author Marco
 *
 */

public class IxigoReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource implements IxigoMessageResource {
    public String getMessage(String code, String ...args) {
        return this.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
