const Graph = ForceGraph3D()
(document.getElementById("3d-graph"));



Graph
  .nodeLabel('id')
  .nodeAutoColorBy('group')
  .numDimensions(2)
  .linkWidth(2)
  .nodeColor(n => {
        return d3.interpolatePlasma(n.group/8.0);
    })
  .nodeVal(n => {return n.group + 1;})
  .forceEngine('ngraph')
  .jsonUrl('http://localhost:9000/rest/simple/graph');

