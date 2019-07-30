package com.apigcc.common.markup;

import com.apigcc.common.markup.asciidoc.AsciiDocBuilder;
import com.apigcc.common.markup.markdown.MarkdownBuilder;

import java.util.List;
import java.util.function.Consumer;

/**
 * 文档构建器
 * 只实现满足需求的部分即可
 */
public interface MarkupBuilder {

    static MarkupBuilder getInstance(){
        return new AsciiDocBuilder();
    }

    /**
     * 文档头
     * @param text 标题
     * @param attrs 文档属性
     * @return this builder
     */
    MarkupBuilder header(String text, CharSequence... attrs);

    /**
     * 文档标题
     * @param level [1,5]
     * @param text 标题
     * @return this builder
     */
    MarkupBuilder title(int level, String text);

    /**
     * 行内文字
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder text(String text);

    /**
     * 单行文字
     * @param text text 换行将被替换为空格
     * @return this builder
     */
    MarkupBuilder textLine(String text);

    /**
     * 多行文字
     * @param text text
     * @return this builder
     */
    MarkupBuilder paragraph(String text, CharSequence... attrs);
    /**
     * 带icon的各种段落
     */
    MarkupBuilder note(String text);

    MarkupBuilder tip(String text);

    MarkupBuilder important(String text);

    MarkupBuilder warning(String text);

    MarkupBuilder caution(String text);

    /**
     * 各种block
     */
    MarkupBuilder block(Consumer<MarkupBuilder> consumer, CharSequence flag, CharSequence... attrs);

    MarkupBuilder listing(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    MarkupBuilder literal(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    MarkupBuilder sidebar(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    MarkupBuilder comment(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    MarkupBuilder passthrough(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    MarkupBuilder quote(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    MarkupBuilder example(Consumer<MarkupBuilder> consumer, CharSequence... attrs);

    /**
     * 列表 默认数字
     * @param text
     * @return
     */
    MarkupBuilder list(String text);

    /**
     * 列表 默认数字
     * @param text
     * @param flag 数字，字母，罗马字母
     * @return
     */
    MarkupBuilder list(String text, CharSequence flag);

    /**
     * 链接
     * @param text
     * @param url
     * @return
     */
    MarkupBuilder url(String text, String url);

    /**
     *
     * @param text
     * @param url
     * @return
     */
    MarkupBuilder image(String text, String url);

    /**
     * 表格 默认第一组数据为表头
     * @param data
     * @return
     */
    MarkupBuilder table(List<List<String>> data);

    MarkupBuilder table(List<List<String>> data, boolean header, boolean footer);


    /**
     * 强调
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder emphasized(String text, CharSequence... textStyle);

    /**
     * 加粗
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder strong(String text, CharSequence... textStyle);

    /**
     * 等宽
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder monospaced(String text, CharSequence... textStyle);

    /**
     * 单引号
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder quoted(String text, CharSequence... textStyle);

    /**
     * 双引号
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder doubleQuoted(String text, CharSequence... textStyle);

    /**
     * 正常的引用文字
     *
     * @param text text
     * @return this builder
     */
    MarkupBuilder unquoted(String text, CharSequence... textStyle);


    /**
     * 换行
     *
     * @return this builder
     */
    MarkupBuilder br();

    /**
     * 强制换行
     *
     * @return this builder
     */
    MarkupBuilder hbr();

    /**
     * 另起一行
     *
     * @return this builder
     */
    MarkupBuilder newLine();

    /**
     * 横线
     *
     * @return this builder
     */
    MarkupBuilder pageBreak();

    /**
     * 获取文件内容
     * @return
     */
    String getContent();

    /**
     * 清空content
     */
    void clean();

}

