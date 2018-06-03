const Graph = ForceGraph3D()
(document.getElementById("3d-graph"));

Graph
  .nodeAutoColorBy('group')
  .linkWidth(2)
  .numDimensions(3)
  .jsonUrl('http://localhost:9000/rest/simple/graph')
  .nodeThreeObject(node => {
      const sprite = new SpriteText(node.id);
      sprite.color = node.color;
      sprite.textHeight = 3+node.group*2;
      return sprite;
  });


Graph.d3Force('charge').strength(-150);