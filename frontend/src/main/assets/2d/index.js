const Graph = ForceGraph3D()
(document.getElementById("3d-graph"));



Graph
  .nodeLabel('id')
  .nodeAutoColorBy('group')
  .numDimensions(2)
  .linkWidth(2)
  .nodeThreeObject(
      node => {
          const sprite = new SpriteText(node.id);
          sprite.color = node.color;
          sprite.textHeight = 8;
          return sprite;
      }
  )
  .nodeColor(n => {
        return d3.interpolatePlasma(n.group/8.0);
    })
  .nodeVal(n => {return n.group + 1;})
  .forceEngine('ngraph')
  .jsonUrl('http://localhost:9000/rest/simple/graph');

Graph.d3Force('charge').strength(-150);
