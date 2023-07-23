package projeto.api.taskmanager.task;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;

//Criada para tests gerais adicionar implementação para test
public class CacheImpl implements Cache{

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public Object getNativeCache() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNativeCache'");
    }

    @Override
    public ValueWrapper get(Object key) {
        return () -> new Object();
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public void put(Object key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public void evict(Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evict'");
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clear'");
    }
    
}
