package com.limaila.bms.cache;

import com.limaila.bms.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 @Author:MrHuang
 @Date: 2019/6/21 15:05
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
@Component
public class RedisUtils {

    private static StringRedisTemplate TEMPLATE;

    private static ValueOperations<String, String> VOS;

    private static HashOperations<String, String, String> HOS;

    private static ListOperations<String, String> LOS;

    private static SetOperations<String, String> SOS;

    private static ZSetOperations<String, String> ZSOS;


    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate template) {
        RedisUtils.TEMPLATE = template;
        RedisUtils.VOS = template.opsForValue();
        RedisUtils.HOS = template.opsForHash();
        RedisUtils.LOS = template.opsForList();
        RedisUtils.SOS = template.opsForSet();
        RedisUtils.ZSOS = template.opsForZSet();
    }


    /***--------------------------- Base ------------------------------- ***/

    /**
     * 删除某个key
     *
     * @param key
     * @return
     */
    public static Boolean delete(String key) {
        try {
            return TEMPLATE.delete(key);
        } catch (Exception e) {
            log.error("RedisUtils del", e);
            return null;
        }
    }

    /**
     * 删除一些key
     *
     * @param keys
     * @return
     */
    public static Long delete(String... keys) {
        return delete(Arrays.asList(keys));
    }

    /**
     * 删除一些key
     *
     * @param keys
     * @return
     */
    public static Long delete(Collection<String> keys) {
        try {
            return TEMPLATE.delete(keys);
        } catch (Exception e) {
            log.error("RedisUtils delKeys");
            return null;
        }
    }

    /**
     * 设置失效时间 单位秒
     *
     * @param key
     * @param timeoutSecond
     * @return
     */
    public static Boolean expire(String key, long timeoutSecond) {
        try {
            return TEMPLATE.expire(key, timeoutSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtils expire", e);
            return null;
        }
    }

    /**
     * 设置失效时间 具体到日期
     *
     * @param key
     * @param date
     * @return
     */
    public static Boolean expireAt(String key, Date date) {
        try {
            return TEMPLATE.expireAt(key, date);
        } catch (Exception e) {
            log.error("RedisUtils expireAt", e);
            return null;
        }
    }

    /**
     * 获取Key的类型
     *
     * @param key
     * @return
     */
    public static DataType type(String key) {
        try {
            return TEMPLATE.type(key);
        } catch (Exception e) {
            log.error("RedisUtils type", e);
            return null;
        }
    }

    /**
     * 判断是否key存在
     *
     * @param key
     * @return
     */
    public static Boolean hasKey(String key) {
        try {
            return TEMPLATE.hasKey(key);
        } catch (Exception e) {
            log.error("RedisUtils exists", e);
            return null;
        }
    }

    /**
     * 获取失效时间
     *
     * @param key
     * @return
     */
    public static Long getExpire(String key) {
        try {
            return TEMPLATE.getExpire(key);
        } catch (Exception e) {
            log.error("RedisUtils getExpire", e);
            return null;
        }
    }


    /***--------------------------- String ------------------------------- ***/

    /**
     * Redis Get
     *
     * @param key
     * @return
     */
    public static String getForString(String key) {
        try {
            return VOS.get(key);
        } catch (Exception e) {
            log.error("RedisUtils get", e);
            return null;
        }
    }

    public static <T> T getObjectForString(String key, Class<T> clazz) {
        return JSONUtils.toBean(getForString(key), clazz);
    }

    public static <T> List<T> getArrayForString(String key, Class<T> clazz) {
        return JSONUtils.toList(getForString(key), clazz);
    }


    public static boolean setForString(String key, String value) {
        try {
            VOS.set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils set", e);
            return false;
        }
    }

    public static boolean setForString(String key, Object value) {
        return setForString(key, JSONUtils.toString(value));
    }

    public static boolean setForString(String key, Object value, int expire) {
        return setForString(key, JSONUtils.toString(value), expire);
    }

    public static boolean setForString(String key, String value, int expire) {
        try {
            VOS.set(key, value, expire, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils set", e);
            return false;
        }
    }

    public static boolean setIfAbsentForString(String key, String value) {
        try {
            return Optional.ofNullable(VOS.setIfAbsent(key, value)).orElse(false);
        } catch (Exception e) {
            log.error("RedisUtils setnx", e);
            return false;
        }
    }

    public static Long incrementForString(String key, long delta) {
        try {
            return VOS.increment(key, delta);
        } catch (Exception e) {
            log.error("RedisUtils incr", e);
            return null;
        }
    }


    public static Double incrementForString(String key, double delta) {
        try {
            return VOS.increment(key, delta);
        } catch (Exception e) {
            log.error("RedisUtil incr", e);
            return null;
        }
    }

    public static Long decrementForString(String key, long delta) {
        return incrementForString(key, -delta);
    }


    public static Double decrementForString(String key, double delta) {
        return incrementForString(key, -delta);
    }


    public static List<String> multiGetForString(String... keys) {
        return multiGetForString(Arrays.asList(keys));
    }

    public static List<String> multiGetForString(Collection<String> keys) {
        try {
            return VOS.multiGet(keys);
        } catch (Exception e) {
            log.error("RedisUtils mget", e);
            return null;
        }
    }

    public static boolean multiSetForString(Map<String, String> map) {
        try {
            VOS.multiSet(map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils mset", e);
            return false;
        }
    }

    public static String getAndSetForString(String key, String value) {
        try {
            return VOS.getAndSet(key, value);
        } catch (Exception e) {
            log.error("RedisUtils getAndSet", e);
            return null;
        }
    }


    public static Long sizeForString(String key) {
        try {
            return VOS.size(key);
        } catch (Exception e) {
            log.error("RedisUtils size", e);
            return null;
        }
    }

    public static Boolean multiSetIfAbsentForString(Map<String, String> map) {
        try {
            return VOS.multiSetIfAbsent(map);
        } catch (Exception e) {
            log.error("RedisUtil msetnx", e);
            return null;
        }
    }


    /***--------------------------- Hash ------------------------------- ***/

    public static Long deleteForHash(String key, String... hashKeys) {
        try {
            return HOS.delete(key, hashKeys);
        } catch (Exception e) {
            log.error("RedisUtils deleteForHash", e);
            return null;
        }
    }

    public Boolean hasKeyForHash(String key, String hashKey) {
        try {
            return HOS.hasKey(key, hashKey);
        } catch (Exception e) {
            log.error("RedisUtils hasKeyForHash", e);
            return null;
        }
    }

    public static String getOneForHash(String key, String hashKey) {
        try {
            return HOS.get(key, hashKey);
        } catch (Exception e) {
            log.error("RedisUtils getOneForHash", e);
            return null;
        }
    }

    public static List<String> getManyForHash(String key, Collection<String> hashKeys) {
        try {
            return HOS.multiGet(key, hashKeys);
        } catch (Exception e) {
            log.error("RedisUtils getManyForHash");
            return null;
        }
    }

    public static List<String> getManyForHash(String key, String... hashKeys) {
        return getManyForHash(key, Arrays.asList(hashKeys));
    }

    public static Long incrForHash(String key, String hashKey, long delta) {
        try {
            return HOS.increment(key, hashKey, delta);
        } catch (Exception e) {
            log.error("RedisUtils incrForHash");
            return null;
        }
    }

    public static Double incrForHash(String key, String hashKey, double delta) {
        try {
            return HOS.increment(key, hashKey, delta);
        } catch (Exception e) {
            log.error("RedisUtils incrForHash");
            return null;
        }
    }

    public static Long getSizeForHash(String key) {
        try {
            return HOS.size(key);
        } catch (Exception e) {
            log.error("RedisUtils getSizeForHash");
            return null;
        }
    }

    public static boolean putManyForHash(String key, Map<String, String> map) {
        try {
            HOS.putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils putManyForHash");
            return false;
        }
    }

    public static boolean putOneForHash(String key, String hashKey, String value) {
        try {
            HOS.put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils putOneForHash", e);
            return false;
        }
    }


    public static List<String> getHashKeyForHash(String key) {
        try {
            return HOS.values(key);
        } catch (Exception e) {
            log.error("RedisUtils getHashKeyForHash", e);
            return null;
        }
    }

    public static Map<String, String> getHashKeyValueForHash(String key) {
        try {
            return HOS.entries(key);
        } catch (Exception e) {
            log.error("RedisUtils getHashKeyValueForHash", e);
            return null;
        }
    }

    public static List<Map.Entry<String, String>> getEntryForHash(String key) {
        try {
            List<Map.Entry<String, String>> entryList = new ArrayList<>();
            Cursor<Map.Entry<String, String>> curson = HOS.scan(key, ScanOptions.NONE);
            while (curson.hasNext()) {
                Map.Entry<String, String> entry = curson.next();
                entryList.add(entry);
            }
            return entryList;
        } catch (Exception e) {
            return null;
        }
    }


    /***--------------------------- List ------------------------------- ***/

    /**
     * 往左put值
     *
     * @param key
     * @param value
     * @return
     */
    public static Long leftPushForList(String key, String value) {
        try {
            return LOS.leftPush(key, value);
        } catch (Exception e) {
            log.error("RedisUtils leftPushForList", e);
            return null;
        }
    }

    /**
     * 将value值放在pivot值得左边
     *
     * @param key   key
     * @param pivot pivot
     * @param value value
     * @return
     */
    public static Long leftPushForList(String key, String pivot, String value) {
        try {
            return LOS.leftPush(key, pivot, value);
        } catch (Exception e) {
            log.error("RedisUtils leftPushForList", e);
            return null;
        }
    }


    public static String leftPopForList(String key) {
        try {
            return LOS.leftPop(key);
        } catch (Exception e) {
            log.error("RedisUtils leftPopForList", e);
            return null;
        }
    }

    public static String leftPopForList(String key, long timeout) {
        try {
            return LOS.leftPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtils leftPopForList", e);
            return null;
        }
    }

    public static Long leftPushAllForList(String key, String... values) {
        try {
            return LOS.leftPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils leftPushAllForList", e);
            return null;
        }
    }

    public static Long leftPushAllForLisr(String key, Collection<String> values) {
        try {
            return LOS.leftPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils leftPushAllForLisr", e);
            return null;
        }
    }


    public static Long leftPushIfPresentForList(String key, String value) {
        try {
            return LOS.leftPushIfPresent(key, value);
        } catch (Exception e) {
            log.error("RedisUtils leftPushIfPresentForList", e);
            return null;
        }
    }


    public static Long rightPushForList(String key, String value) {
        try {
            return LOS.rightPush(key, value);
        } catch (Exception e) {
            log.error("RedisUtils rightPushForList", e);
            return null;
        }
    }


    /**
     * 将value值放在pivot值得右边
     *
     * @param key   key
     * @param pivot pivot
     * @param value value
     * @return
     */
    public static Long rightPushForList(String key, String pivot, String value) {
        try {
            return LOS.rightPush(key, pivot, value);
        } catch (Exception e) {
            log.error("RedisUtils rightPushForList", e);
            return null;
        }
    }


    public static String rightPopForList(String key) {
        try {
            return LOS.rightPop(key);
        } catch (Exception e) {
            log.error("RedisUtils rightPopForList", e);
            return null;
        }
    }

    public static String rightPopForList(String key, long timeout) {
        try {
            return LOS.rightPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtils rightPopForList", e);
            return null;
        }
    }


    public static Long rightPushAllForList(String key, String... values) {
        try {
            return LOS.rightPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils rightPushAllForList", e);
            return null;
        }
    }

    public static Long rightPushAllForList(String key, Collection<String> values) {
        try {
            return LOS.rightPushAll(key, values);
        } catch (Exception e) {
            log.error("RedisUtils rightPushAllForList", e);
            return null;
        }
    }

    public static Long rightPushIfPresentForList(String key, String value) {
        try {
            return LOS.rightPushIfPresent(key, value);
        } catch (Exception e) {
            log.error("RedisUtils rightPushIfPresentForList", e);
            return null;
        }
    }


    public static List<String> lrangeForList(String key, long start, long end) {
        try {
            return LOS.range(key, start, end);
        } catch (Exception e) {
            log.error("RedisUtils lrangeForList", e);
            return null;
        }
    }

    /**
     * lrem [lrem key count value] :移除等于value的元素，
     * 当count>0时，从表头开始查找，移除count个 移除等于value的元素；
     * 当count=0时，从表头开始查找，移除所有等于value的；
     * 当count<0时，从表尾开始查找，移除count个 移除等于value的元素。
     *
     * @param key   key
     * @param count count
     * @param value value
     * @return
     */
    public static Long removeForList(String key, long count, String value) {
        try {
            return LOS.remove(key, count, value);
        } catch (Exception e) {
            log.error("RedisUtils removeForList", e);
            return null;
        }
    }


    /**
     * 根据下表获取列表中的值，下标是从0开始的
     *
     * @param key   key
     * @param index index
     * @return
     */
    public static String indexForList(String key, long index) {
        try {
            return LOS.index(key, index);
        } catch (Exception e) {
            log.error("RedisUtils indexForList", e);
            return null;
        }
    }

    /**
     * 将value的值设置到列表index位置
     * 数组不能越界
     *
     * @param key   key
     * @param index index
     * @param value value
     * @return
     */
    public static boolean setForList(String key, long index, String value) {
        try {
            LOS.set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils setForList", e);
            return false;
        }
    }

    /**
     * 修剪现有列表，使其只保留指定的指定范围的元素，起始和停止都是基于0的索引
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return
     */
    public static boolean trimForList(String key, long start, long end) {
        try {
            LOS.trim(key, start, end);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils trimForList", e);
            return false;
        }
    }


    /***--------------------------- Set ------------------------------- ***/

    /**
     * SADD key member1 [member2]
     * 向集合添加一个或多个成员
     *
     * @param key
     * @param values
     * @return
     */
    public static Long addForSet(String key, String... values) {
        try {
            return SOS.add(key, values);
        } catch (Exception e) {
            log.error("RedisUtils addForSet", e);
            return null;
        }
    }

    /**
     * SREM key member1 [member2]
     * 移除集合中一个或多个成员
     *
     * @param key
     * @param values
     * @return
     */
    public static Long removeForSet(String key, String... values) {
        try {
            return SOS.remove(key, (Object[]) values);
        } catch (Exception e) {
            log.error("RedisUtils removeForSet", e);
            return null;
        }
    }

    /**
     * SPOP key
     * 移除并返回集合中的一个随机元素
     *
     * @param key
     * @return
     */
    public static String popForSet(String key) {
        try {
            return SOS.pop(key);
        } catch (Exception e) {
            log.error("RedisUtils popForSet", e);
            return null;
        }
    }

    /**
     * SPOP key
     * 移除并返回集合中的多个随机元素
     *
     * @param key
     * @return
     */
    public static List<String> popForSet(String key, long count) {
        try {
            return SOS.pop(key, count);
        } catch (Exception e) {
            log.error("RedisUtils popForSet", e);
            return null;
        }
    }

    /**
     * SMOVE source destination member
     * 将 member 元素从 source 集合移动到 destination 集合
     *
     * @param key
     * @param value
     * @param destKey
     * @return
     */
    public static Boolean moveForSet(String key, String value, String destKey) {
        try {
            return SOS.move(key, value, destKey);
        } catch (Exception e) {
            log.error("RedisUtils moveForSet", e);
            return null;
        }
    }

    /**
     * SCARD key
     * 获取集合的成员数
     *
     * @param key
     * @return
     */
    public static Long sizeForSet(String key) {
        try {
            return SOS.size(key);
        } catch (Exception e) {
            log.error("RedisUtils sizeForSet", e);
            return null;
        }
    }

    /**
     * SISMEMBER key member
     * 判断 member 元素是否是集合 key 的成员
     *
     * @param key
     * @param o
     * @return
     */
    public static Boolean isMemberForSet(String key, Object o) {
        try {
            return SOS.isMember(key, o);
        } catch (Exception e) {
            log.error("RedisUtils isMemberForSet", e);
            return null;
        }
    }

    /**
     * SDIFF key1 [key2]
     * 返回给定所有集合的差集
     *
     * @param key
     * @param otherKey
     * @return
     */
    public static Set<String> differenceForSet(String key, String otherKey) {
        try {
            return SOS.difference(key, otherKey);
        } catch (Exception e) {
            log.error("RedisUtils differenceForSet", e);
            return null;
        }
    }


    /***--------------------------- ZSet ------------------------------- ***/


    /**
     * ZADD key score1 member1 [score2 member2]
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static boolean addForZSet(String key, String value, double score) {
        try {
            return Optional.ofNullable(ZSOS.add(key, value, score)).orElse(false);
        } catch (Exception e) {
            log.error("RedisUtils addForZSet", e);
        }
        return false;
    }

    /**
     * ZADD key score1 member1 [score2 member2]
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     *
     * @param key
     * @param tuples
     * @return
     */
    public static Long addForZSet(String key, Set<ZSetOperations.TypedTuple<String>> tuples) {
        return ZSOS.add(key, tuples);
    }

    /**
     * ZCARD key
     * 获取有序集合的成员数
     *
     * @param key
     * @return
     */
    public static Long sizeForZSet(String key) {
        try {
            return ZSOS.size(key);
        } catch (Exception e) {
            log.error("RedisUtils sizeForZSet", e);
        }
        return null;
    }

    /**
     * ZCOUNT key min max
     * 计算在有序集合中指定区间分数的成员数
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long countForZSet(String key, double min, double max) {
        return ZSOS.count(key, min, max);
    }

    /**
     * ZINCRBY key increment member
     * 有序集合中对指定成员的分数加上增量 increment
     *
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public static Double incrementScoreForZSet(String key, String value, double delta) {
        return ZSOS.incrementScore(key, value, delta);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> rangeByScoreForZSet(String key, double min, double max) {
        return ZSOS.rangeByScore(key, min, max);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<String> rangeByScoreForZSet(String key, double min, double max, long offset, long count) {
        return ZSOS.rangeByScore(key, min, max, offset, count);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScoresForZSet(String key, double min, double max) {
        return ZSOS.rangeByScoreWithScores(key, min, max);
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScoresForZSet(String key, double min, double max, long offset, long count) {
        return ZSOS.rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * ZRANK key member
     * 返回有序集合中指定成员的索引
     *
     * @param key
     * @param value
     * @return
     */
    public static Long rankForZSet(String key, Object value) {
        return ZSOS.rank(key, value);
    }

    /**
     * ZREM key member [member ...]
     * 移除有序集合中的一个或多个成员
     *
     * @param key
     * @param values
     * @return
     */
    public static Long removeForZSet(String key, Object... values) {
        return ZSOS.remove(key, values);
    }

    /**
     * ZREMRANGEBYLEX key min max
     * 移除有序集合中给定的字典区间的所有成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long removeRangeForZSet(String key, long start, long end) {
        return ZSOS.removeRange(key, start, end);
    }

    /**
     * ZREMRANGEBYSCORE key min max
     * 移除有序集合中给定的分数区间的所有成员
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long removeRangeByScoreForZSet(String key, double min, double max) {
        return ZSOS.removeRangeByScore(key, min, max);
    }

    /**
     * ZREVRANGE key start stop [WITHSCORES]
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> reverseRangeForZSet(String key, long start, long end) {
        return ZSOS.reverseRange(key, start, end);
    }

    /**
     * ZREVRANGE key start stop [WITHSCORES]
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScoresForZSet(String key, long start, long end) {
        return ZSOS.reverseRangeWithScores(key, start, end);
    }

    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> reverseRangeByScoreForZSet(String key, double min, double max) {
        return ZSOS.reverseRangeByScore(key, min, max);
    }

    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> reverseRangeByScoreForZSet(String key, double min, double max, long offset, long count) {
        return ZSOS.reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScoresForZSet(String key, double min, double max) {
        return ZSOS.reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScoresForZSet(String key, double min, double max, long offset, long count) {
        return ZSOS.reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * ZREVRANK key member
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     *
     * @param key
     * @param value
     * @return
     */
    public static Long reverseRankForZSet(String key, Object value) {
        return ZSOS.reverseRank(key, value);
    }

    /**
     * ZSCORE key member
     * 返回有序集中，成员的分数值
     *
     * @param key
     * @param value
     * @return
     */
    public static Double scoreForZSet(String key, Object value) {
        return ZSOS.score(key, value);
    }

    /**
     * ZSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代有序集合中的元素（包括元素成员和元素分值）
     *
     * @param key
     * @param options
     * @return
     */
    public static Cursor<ZSetOperations.TypedTuple<String>> scanForZSet(String key, ScanOptions options) {
        return ZSOS.scan(key, options);
    }
}
