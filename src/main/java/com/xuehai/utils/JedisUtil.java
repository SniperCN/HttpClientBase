package com.xuehai.utils;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName JedisUtil
 * @Description:    redis工具类
 * @Author Sniper
 * @Date 2019/4/19 9:18
 */
public class JedisUtil {

    private static Jedis jedis;

    /**
     *
     * @Description: 	获取redis实例
     * @param host		主机名
     * @param port		端口
     * @return Jedis
     */
    public static Jedis getJedis(String host, int port) {
        if (jedis == null) {
            jedis = new Jedis(host, port);
        }
        return jedis;
    }

    /**
     *
     * @Description: 	关闭连接
     * @return void
     */
    public static void close() {
        if (jedis != null) {
            jedis.close();
        }
    }



    /**
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public static String get(String host, int port, String key) {
        jedis = getJedis(host, port);
        String value = jedis.get(key);
        close();
        return value;
    }

    /**
     * 设置key的值为value
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String host, int port, String key, String value) {
        jedis = getJedis(host, port);
        String statusReply = jedis.set(key, value);
        close();
        return statusReply;
    }

    /**
     * 删除指定的key,也可以传入一个包含key的数组
     *
     * @param keys
     * @return
     */
    public static Long del(String host, int port,String... keys) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.del(keys);
        close();
        return statusReply;
    }

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    public static Long append(String host, int port, String key, String str) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.append(key, str);
        close();
        return statusReply;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static Boolean exists(String host, int port, String key) {
        jedis = getJedis(host, port);
        Boolean exists = jedis.exists(key);
        close();
        return exists;
    }

    /**
     * 设置key value,如果key已经存在则返回0
     *
     * @param key
     * @param value
     * @return
     */
    public static Long setnx(String host, int port, String key, String value) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.setnx(key, value);
        close();
        return statusReply;
    }

    /**
     * 设置key value并指定这个键值的有效期
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public static String setex(String host, int port, String key, int seconds, String value) {
        jedis = getJedis(host, port);
        String statusReply = jedis.setex(key, seconds, value);
        close();
        return statusReply;
    }

    /**
     * 通过key 和offset 从指定的位置开始将原先value替换
     *
     * @param key
     * @param offset
     * @param str
     * @return
     */
    public static Long setrange(String host, int port, String key, int offset, String str) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.setrange(key, offset, str);
        close();
        return statusReply;
    }

    /**
     * 通过批量的key获取批量的value
     *
     * @param keys
     * @return
     */
    public static List<String> mget(String host, int port, String... keys) {
        jedis = getJedis(host, port);
        List<String> value = jedis.mget(keys);
        close();
        return value;
    }

    /**
     * 批量的设置key:value,也可以一个
     *
     * @param keysValues
     * @return
     */
    public static String mset(String host, int port,String... keysValues) {
        jedis = getJedis(host, port);
        String value = jedis.mset(keysValues);
        close();
        return value;
    }

    /**
     * 批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚
     *
     * @param keysValues
     * @return
     */
    public static Long msetnx(String host, int port,String... keysValues) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.msetnx(keysValues);
        close();
        return statusReply;
    }

    /**
     * 设置key的值,并返回一个旧值
     *
     * @param key
     * @param value
     * @return
     */
    public static String getSet(String host, int port, String key, String value) {
        jedis = getJedis(host, port);
        String resultValue = jedis.getSet(key, value);
        close();
        return resultValue;
    }

    /**
     * 通过下标 和key 获取指定下标位置的 value
     *
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public static String getrange(String host, int port, String key, int startOffset, int endOffset) {
        jedis = getJedis(host, port);
        String value = jedis.getrange(key, startOffset, endOffset);
        close();
        return value;
    }

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     *
     * @param key
     * @return
     */
    public static Long incr(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.incr(key);
        close();
        return statusReply;
    }

    /**
     * 通过key给指定的value加值,如果key不存在,则这是value为该值
     *
     * @param key
     * @param integer
     * @return
     */
    public static Long incrBy(String host, int port, String key, long integer) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.incrBy(key, integer);
        close();
        return statusReply;
    }

    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1
     *
     * @param key
     * @return
     */
    public static Long decr(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.decr(key);
        close();
        return statusReply;
    }

    /**
     * 减去指定的值
     *
     * @param key
     * @param integer
     * @return
     */
    public static Long decrBy(String host, int port, String key, long integer) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.decrBy(key, integer);
        close();
        return statusReply;
    }

    /**
     * 通过key获取value值的长度
     *
     * @param key
     * @return
     */
    public static Long strLen(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.strlen(key);
        close();
        return statusReply;
    }

    /**
     * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hsetnx(String host, int port, String key, String field, String value) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.hsetnx(key, field, value);
        close();
        return statusReply;
    }

    /**
     * 通过key给field设置指定的值,如果key不存在,则先创建
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hset(String host, int port, String key, String field, String value) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.hset(key, field, value);
        close();
        return statusReply;
    }

    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public static String hmset(String host, int port, String key, Map<String, String> hash) {
        jedis = getJedis(host, port);
        String value = jedis.hmset(key, hash);
        close();
        return value;
    }

    /**
     * 通过key 和 field 获取指定的 value
     *
     * @param key
     * @param failed
     * @return
     */
    public static String hget(String host, int port, String key, String failed) {
        jedis = getJedis(host, port);
        String value = jedis.hget(key, failed);
        close();
        return value;
    }

    /**
     * 设置key的超时时间为seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    public static Long expire(String host, int port, String key, int seconds) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.expire(key, seconds);
        close();
        return statusReply;
    }

    /**
     * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
     *
     * @param key
     * @param fields 可以是 一个String 也可以是 String数组
     * @return
     */
    public static List<String> hmget(String host, int port, String key, String... fields) {
        jedis = getJedis(host, port);
        List<String> value = jedis.hmget(key, fields);
        close();
        return value;
    }

    /**
     * 通过key给指定的field的value加上给定的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hincrby(String host, int port, String key, String field, Long value) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.hincrBy(key, field, value);
        close();
        return statusReply;
    }

    /**
     * 通过key和field判断是否有指定的value存在
     *
     * @param key
     * @param field
     * @return
     */
    public static Boolean hexists(String host, int port, String key, String field) {
        jedis = getJedis(host, port);
        Boolean hexists = jedis.hexists(key, field);
        close();
        return hexists;
    }

    /**
     * 通过key返回field的数量
     *
     * @param key
     * @return
     */
    public static Long hlen(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.hlen(key);
        close();
        return statusReply;
    }

    /**
     * 通过key 删除指定的 field
     *
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public static Long hdel(String host, int port, String key, String... fields) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.hdel(key, fields);
        close();
        return statusReply;
    }

    /**
     * 通过key返回所有的field
     *
     * @param key
     * @return
     */
    public static Set<String> hkeys(String host, int port, String key) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.hkeys(key);
        close();
        return value;
    }

    /**
     * 通过key返回所有和key有关的value
     *
     * @param key
     * @return
     */
    public static List<String> hvals(String host, int port, String key) {
        jedis = getJedis(host, port);
        List<String> value = jedis.hvals(key);
        close();
        return value;
    }

    /**
     * 通过key获取所有的field和value
     *
     * @param key
     * @return
     */
    public static Map<String, String> hgetall(String host, int port, String key) {
        jedis = getJedis(host, port);
        Map<String, String> value = jedis.hgetAll(key);
        close();
        return value;
    }

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public static Long lpush(String host, int port, String key, String... strs) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.lpush(key, strs);
        close();
        return statusReply;
    }

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public static Long rpush(String host, int port, String key, String... strs) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.rpush(key, strs);
        close();
        return statusReply;
    }

    /**
     * 通过key在list指定的位置之前或者之后 添加字符串元素
     *
     * @param key
     * @param where LIST_POSITION枚举类型
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public static Long linsert(String host, int port, String key, BinaryClient.LIST_POSITION where,
                        String pivot, String value) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.linsert(key, where, pivot, value);
        close();
        return statusReply;
    }

    /**
     * 通过key设置list指定下标位置的value
     * 如果下标超过list里面value的个数则报错
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public static String lset(String host, int port, String key, Long index, String value) {
        jedis = getJedis(host, port);
        String resultValue = jedis.lset(key, index, value);
        close();
        return resultValue;
    }

    /**
     * 通过key从对应的list中删除指定的count个 和 value相同的元素
     *
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public static Long lrem(String host, int port, String key, long count, String value) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.lrem(key, count, value);
        close();
        return statusReply;
    }

    /**
     * 通过key保留list中从strat下标开始到end下标结束的value值
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public static String ltrim(String host, int port, String key, long start, long end) {
        jedis = getJedis(host, port);
        String value = jedis.ltrim(key, start, end);
        close();
        return value;
    }

    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public static synchronized String lpop(String host, int port, String key) {
        jedis = getJedis(host, port);
        String value = jedis.lpop(key);
        close();
        return value;
    }

    /**
     * 通过key从list尾部删除一个value,并返回该元素
     *
     * @param key
     * @return
     */
    public static synchronized String rpop(String host, int port, String key) {
        jedis = getJedis(host, port);
        String value = jedis.rpop(key);
        close();
        return value;
    }

    /**
     * 通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value
     * 如果第一个list为空或者不存在则返回null
     *
     * @param srckey
     * @param dstkey
     * @return
     */
    public static String rpoplpush(String host, int port,String srckey, String dstkey) {
        jedis = getJedis(host, port);
        String value = jedis.rpoplpush(srckey, dstkey);
        close();
        return value;
    }

    /**
     * 通过key获取list中指定下标位置的value
     *
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public static String lindex(String host, int port, String key, long index) {
        jedis = getJedis(host, port);
        String value = jedis.lindex(key, index);
        close();
        return value;
    }

    /**
     * 通过key返回list的长度
     *
     * @param key
     * @return
     */
    public static Long llen(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.llen(key);
        close();
        return statusReply;
    }

    /**
     * 通过key获取list指定下标位置的value
     * 如果start 为 0 end 为 -1 则返回全部的list中的value
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<String> lrange(String host, int port, String key, long start, long end) {
        jedis = getJedis(host, port);
        List<String> value = jedis.lrange(key, start, end);
        close();
        return value;
    }

    /**
     * 通过key向指定的set中添加value
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public static Long sadd(String host, int port, String key, String... members) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.sadd(key, members);
        close();
        return statusReply;
    }

    /**
     * 通过key删除set中对应的value值
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public static Long srem(String host, int port, String key, String... members) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.srem(key, members);
        close();
        return statusReply;
    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key
     * @return
     */
    public static String spop(String host, int port, String key) {
        jedis = getJedis(host, port);
        String value = jedis.spop(key);
        close();
        return value;
    }

    /**
     * 通过key获取set中的差集
     * 以第一个set为标准
     *
     * @param keys 可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public static Set<String> sdiff(String host, int port,String... keys) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.sdiff(keys);
        close();
        return value;
    }

    /**
     * 通过key获取set中的差集并存入到另一个key中
     * 以第一个set为标准
     *
     * @param dstkey 差集存入的key
     * @param keys   可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public static Long sdiffstore(String host, int port,String dstkey, String... keys) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.sdiffstore(dstkey, keys);
        close();
        return statusReply;
    }

    /**
     * 通过key获取指定set中的交集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Set<String> sinter(String host, int port, String... keys) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.sinter(keys);
        close();
        return value;
    }

    /**
     * 通过key获取指定set中的交集 并将结果存入新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Long sinterstore(String host, int port,String dstkey, String... keys) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.sinterstore(dstkey, keys);
        close();
        return statusReply;
    }

    /**
     * 通过key返回所有set的并集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Set<String> sunion(String host, int port,String... keys) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.sunion(keys);
        close();
        return value;
    }

    /**
     * 通过key返回所有set的并集,并存入到新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Long sunionstore(String host, int port,String dstkey, String... keys) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.sunionstore(dstkey, keys);
        close();
        return statusReply;
    }

    /**
     * 通过key将set中的value移除并添加到第二个set中
     *
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     * @return
     */
    public static Long smove(String host, int port,String srckey, String dstkey, String member) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.smove(srckey, dstkey, member);
        close();
        return statusReply;
    }

    /**
     * 通过key获取set中value的个数
     *
     * @param key
     * @return
     */
    public static Long scard(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.scard(key);
        close();
        return statusReply;
    }

    /**
     * 通过key判断value是否是set中的元素
     *
     * @param key
     * @param member
     * @return
     */
    public static Boolean sismember(String host, int port, String key, String member) {
        jedis = getJedis(host, port);
        Boolean sismember = jedis.sismember(key, member);
        close();
        return sismember;
    }

    /**
     * 通过key获取set中随机的value,不删除元素
     *
     * @param key
     * @return
     */
    public static String srandmember(String host, int port, String key) {
        jedis = getJedis(host, port);
        String value = jedis.srandmember(key);
        close();
        return value;
    }

    /**
     * 通过key获取set中所有的value
     *
     * @param key
     * @return
     */
    public static Set<String> smembers(String host, int port, String key) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.smembers(key);
        close();
        return value;
    }


    /**
     * 通过key向zset中添加value,score,其中score就是用来排序的
     * 如果该value已经存在则根据score更新元素
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public static Long zadd(String host, int port, String key, double score, String member) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zadd(key, score, member);
        close();
        return statusReply;
    }

    /**
     * 通过key删除在zset中指定的value
     *
     * @param key
     * @param members 可以 是一个string 也可以是一个string数组
     * @return
     */
    public static Long zrem(String host, int port, String key, String... members) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zrem(key, members);
        close();
        return statusReply;
    }

    /**
     * 通过key增加该zset中value的score的值
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public static Double zincrby(String host, int port, String key, double score, String member) {
        jedis = getJedis(host, port);
        Double value = jedis.zincrby(key, score, member);
        close();
        return value;
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从小到大排序
     *
     * @param key
     * @param member
     * @return
     */
    public static Long zrank(String host, int port, String key, String member) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zrank(key, member);
        close();
        return statusReply;
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从大到小排序
     *
     * @param key
     * @param member
     * @return
     */
    public static Long zrevrank(String host, int port, String key, String member) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zrevrank(key, member);
        close();
        return statusReply;
    }

    /**
     * 通过key将获取score从start到end中zset的value
     * socre从大到小排序
     * 当start为0 end为-1时返回全部
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> zrevrange(String host, int port, String key, long start, long end) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.zrevrange(key, start, end);
        close();
        return value;
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public static Set<String> zrangebyscore(String host, int port, String key, String max, String min) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.zrevrangeByScore(key, max, min);
        close();
        return value;
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public static Set<String> zrangeByScore(String host, int port, String key, double max, double min) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.zrevrangeByScore(key, max, min);
        close();
        return value;
    }

    /**
     * 返回指定区间内zset中value的数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long zcount(String host, int port, String key, String min, String max) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zcount(key, min, max);
        close();
        return statusReply;
    }

    /**
     * 通过key返回zset中的value个数
     *
     * @param key
     * @return
     */
    public static Long zcard(String host, int port, String key) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zcard(key);
        close();
        return statusReply;
    }

    /**
     * 通过key获取zset中value的score值
     *
     * @param key
     * @param member
     * @return
     */
    public static Double zscore(String host, int port, String key, String member) {
        jedis = getJedis(host, port);
        Double value = jedis.zscore(key, member);
        close();
        return value;
    }

    /**
     * 通过key删除给定区间内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long zremrangeByRank(String host, int port, String key, long start, long end) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zremrangeByRank(key, start, end);
        close();
        return statusReply;
    }

    /**
     * 通过key删除指定score内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long zremrangeByScore(String host, int port, String key, double start, double end) {
        jedis = getJedis(host, port);
        Long statusReply = jedis.zremrangeByScore(key, start, end);
        close();
        return statusReply;
    }

    /**
     * 返回满足pattern表达式的所有key
     * keys(*)
     * 返回所有的key
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String host, int port, String pattern) {
        jedis = getJedis(host, port);
        Set<String> value = jedis.keys(pattern);
        close();
        return value;
    }

    /**
     * 通过key判断值得类型
     *
     * @param key
     * @return
     */
    public static String type(String host, int port, String key) {
        jedis = getJedis(host, port);
        String value = jedis.type(key);
        close();
        return value;
    }

}
