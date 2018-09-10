/*
 * Copyright 2018 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamzi.openshift.dataflow.deployment;

import io.streamzi.openshift.dataflow.model.ProcessorFlow;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a set of deployments using a ProcessorFlow
 * @author hhiden
 */
public class FlowDeploymentListBuilder {
    private ProcessorFlow flow;
    private List<String> clouds = new ArrayList<>();
    private String primaryCloudId = "local";
    
    public FlowDeploymentListBuilder(ProcessorFlow flow) {
        this.flow = flow;
    }
    
    public FlowDeploymentListBuilder withPrimaryCloudId(String primaryCloudId){
        this.primaryCloudId = primaryCloudId;
        return this;
    }
    
    public FlowDeploymentListBuilder addCloud(String cloudId){
        if(!clouds.contains(cloudId)){
            clouds.add(cloudId);
        }
        return this;
    }
    
    public FlowDeploymentListBuilder detectClouds(){
        flow.getNodes().stream().forEach(node->node.getTargetClouds().keySet().stream().forEach(cloudId->addCloud(cloudId)));
        return this;
    }
    
    public List<FlowDeployment> build(){
        List<FlowDeployment> results = new ArrayList<>();
        
        for(String cloudId : clouds){
            if(cloudId.equals(primaryCloudId)){
                results.add(new FlowDeployment(cloudId, true, flow));
            } else {
                results.add(new FlowDeployment(cloudId, false, flow));
            }
        }
        return results;
    }
}
