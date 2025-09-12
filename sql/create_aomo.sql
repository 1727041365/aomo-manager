-- 创建城市详情表
CREATE TABLE city_detail (
                             city_detail_id BIGSERIAL PRIMARY KEY, -- 自增主键
                             city_id BIGINT NOT NULL, -- 城市ID
                             city_name VARCHAR(255) NOT NULL, -- 城市名称
                             city_introduce TEXT, -- 城市介绍（长文本）
                             geographical_climate_img VARCHAR(255), -- 地理气候图URL
                             transport_aviation TEXT, -- 航空交通信息
                             transport_railway TEXT, -- 铁路交通信息
                             transport_subway TEXT, -- 地铁交通信息
                             travel_season VARCHAR(255), -- 最佳旅行季节
                             card TEXT, -- 公交卡办理信息
                             room TEXT, -- 住宿推荐
                             consumer_guide TEXT, -- 消费指南
                             create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                             update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 更新时间
                             is_delete INTEGER NOT NULL DEFAULT 0 -- 逻辑删除标识（0-未删除，1-已删除）
);

-- 为city_id和city_name创建索引
CREATE INDEX idx_city_detail_city_id ON city_detail(city_id);
CREATE INDEX idx_city_detail_city_name ON city_detail(city_name);

-- 创建更新时间的自动更新触发器（可选，确保update_date自动更新）
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_date = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_city_detail
    BEFORE UPDATE ON city_detail
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();


-- 创建城市轮播图表
CREATE TABLE city_carousel (
                               city_carousel_id BIGSERIAL PRIMARY KEY, -- 自增主键，对应cityCarouselId
                               city_id BIGINT NOT NULL, -- 城市id，关联城市表
                               city_carousel_category VARCHAR(255) NOT NULL, -- 轮播图分类（如top/middle/bottom）
                               city_carousel_img VARCHAR(255) NOT NULL, -- 轮播图图片URL
                               title VARCHAR(255), -- 轮播图标题
                               description1 TEXT, -- 轮播图描述1
                               description2 TEXT, -- 轮播图描述2
                               description3 TEXT, -- 轮播图描述3
                               description4 TEXT, -- 轮播图描述4
                               create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间，默认当前时间
                               update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 更新时间，默认当前时间
                               is_delete INTEGER NOT NULL DEFAULT 0 -- 逻辑删除标识（0-未删除，1-已删除）
);

-- 单个字段索引
CREATE INDEX idx_city_carousel_city_id ON city_carousel(city_id); -- 按城市id查询
CREATE INDEX idx_city_carousel_category ON city_carousel(city_carousel_category); -- 按分类查询

-- 新增：city_carousel_category 和 city_id 的组合索引
-- 适用于按“城市+分类”查询的场景（如查询某城市的顶部轮播图）
CREATE INDEX idx_city_carousel_city_category ON city_carousel(city_id, city_carousel_category);

-- 自动更新时间的触发器
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_date = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_city_carousel
    BEFORE UPDATE ON city_carousel
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();
