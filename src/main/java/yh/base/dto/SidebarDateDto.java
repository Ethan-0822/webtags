package yh.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: webtags
 * @package: yh.base.Dto
 * @className: SidebarDateDto
 * @author: Ethan
 * @description: 侧边栏日期Dto
 * @version: 1.0
 */

@Data
public class SidebarDateDto implements Serializable {

    private String title;
    private List<SidebarDateDto> children = new ArrayList<>();
}