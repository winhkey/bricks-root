package org.bricks.data.xml.jaxb;

import static org.bricks.utils.DateUtils.format;
import static org.bricks.utils.DateUtils.parse;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.stereotype.Service;

/**
 * LocalDateTime支持
 *
 * @author fuzy
 *
 */
@Service
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
        return format(v);
    }

}
