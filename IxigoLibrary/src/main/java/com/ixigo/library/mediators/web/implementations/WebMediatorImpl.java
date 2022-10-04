package com.ixigo.library.mediators.web.implementations;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import br.com.fluentvalidator.exception.ValidationException;
/**
 * Default implementation of the {@link WebMediatior} interface
 * 
 * @author Marco
 *
 */
public class WebMediatorImpl implements WebMediator {
    private static final Logger _LOGGER = LoggerFactory.getLogger(WebMediatorImpl.class);
    
    private ApplicationContext ctx;

    /**
     * Initializes a new instance of Mediator
     * 
     * @param ctx Application context of Spring
     */
    public WebMediatorImpl(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public <T> ResponseEntity<T> send(WebCommandRequest<T> request) {
        try {
            MediatorPlanRequest<T> plan = new MediatorPlanRequest<>(WebCommandHandler.class, "handle", request.getClass(),
                    ctx);
            return plan.invoke(request);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ValidationException) {
                throw ValidationException.class.cast(e.getTargetException());
            }
            if (e.getTargetException() instanceof IxigoException) {
                throw IxigoException.class.cast(e.getTargetException());
            }
            
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            if(_LOGGER.isDebugEnabled()) {
                e.printStackTrace();
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    class MediatorPlanRequest<T> {
        Method handleMethod;
        Object handlerInstanceBuilder;

        public MediatorPlanRequest(Class<?> handlerType, String handlerMethodName, Class<?> messageType,
                ApplicationContext context) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
            handlerInstanceBuilder = getBean(handlerType, messageType, context);
            handleMethod = handlerInstanceBuilder.getClass().getDeclaredMethod(handlerMethodName, messageType);
        }

        private Object getBean(Class<?> handlerType, Class<?> messageType, ApplicationContext context)
                throws ClassNotFoundException {
            Map<String, ?> beans = context.getBeansOfType(handlerType);
            for (Entry<String, ?> entry : beans.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                Type[] interfaces = clazz.getGenericInterfaces();
                for (Type interace : interfaces) {
                    Type parameterType = ((ParameterizedType) interace).getActualTypeArguments()[0];
                    if (parameterType.equals(messageType)) {
                        return entry.getValue();
                    }
                }
            }

            throw new ClassNotFoundException("Handler not found. Did you forget to register it with the \"@component\" annotation?");
        }

        @SuppressWarnings("unchecked")
        public ResponseEntity<T> invoke(WebCommandRequest<T> request)
                throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return (ResponseEntity<T>) handleMethod.invoke(handlerInstanceBuilder, request);
        }
    }

}
