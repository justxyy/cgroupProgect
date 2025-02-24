package cn.gsq.cgroups.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Project : galaxy
 * Class : cn.gsq.sdp.core.serve.operation.rpc.Position
 *
 * @author : gsq
 * @since : 2021-04-28 10:59
 **/
@Setter
@Getter
@Accessors(chain = true)
public class CgroupParams {

    private String hostname;

    private String process;

    private int port;

    private double cpuLimit;

    private double cpuShare;

    private double memoryLimit;

    private boolean interrupt;

}
