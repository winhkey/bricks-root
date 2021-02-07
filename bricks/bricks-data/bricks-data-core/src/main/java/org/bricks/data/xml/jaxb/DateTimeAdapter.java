package org.bricks.data.xml.jaxb;

import static org.bricks.utils.DateUtils.format;
import static org.bricks.utils.DateUtils.parse;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 序列/反序列化时间
 *
 * @author fuzy
 *
 */
public class DateTimeAdapter extends XmlAdapter<String, LocalDateTime>
{

    @Override
    public LocalDateTime unmarshal(String v)
    {
        return parse(v);
    }

    @Override
    public String marshal(LocalDateTime v)
    {
        return format(v, null);
    }

}
