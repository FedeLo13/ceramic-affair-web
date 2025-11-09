import './About.css'

export default function About() {
    return (
        <div className="about-section">
            <div className="about-block-1">
                <img src="/images/About_1.png" alt="Ceramic Affair Image" className="about-image-1" />                           
            </div>

            <div>
                <div className="about-block">
                    <p className="about-text">
                        I started working with ceramics in 2021, from a small room at home. During the first years, I made every piece entirely by hand, without a wheel or machinery, and I would take my pieces to different studios for firing. That stage helped me understand the process from the ground up and to value every detail of handmade work. Later, I began learning wheel-throwing at Tierraviva, in Jerez —an experience that broadened my perspective on both the material and the craft.
                    </p>
                </div>
                <div className="about-block">
                    <p className="about-text">
                        Empecé en el mundo de la cerámica en 2021, trabajando desde una pequeña habitación de mi casa. Durante los primeros años elaboraba cada pieza de forma completamente manual, sin torno ni maquinaria, y llevaba mis trabajos a distintos talleres para su cocción. Esa etapa me permitió conocer el proceso desde la base y valorar cada detalle del trabajo artesanal. Más adelante, tuve la oportunidad de aprender torno en el taller Tierra Viva de Jerez, con la maestra Heyssel, una experiencia que amplió mi forma de entender el material y la técnica.
                    </p>                            
                </div>
            </div>

            <div className="about-block">
                <img src="/images/About_2.jpeg" alt="Ceramic Affair Image" className="about-image-2" />
                <div>
                    <div className="about-block">
                        <p className="about-text">
                            Over time, I equipped my own studio and began incorporating specialized tools that opened up new possibilities. I also took workshops with well-known ceramic artists, which helped me grow and develop a more personal style. I started taking my pieces to local markets and sharing them with others, something that continues to inspire me. Today, I keep experimenting, searching for a balance between function and form, and enjoying the process behind every piece.
                        </p>
                    </div>

                    <div className="about-block">
                        <p className="about-text">
                            Con el tiempo fui equipando mi propio taller y sumando herramientas que me permitieron explorar nuevas posibilidades. También realicé cursos con ceramistas reconocidos, lo que me ayudó a seguir creciendo y a desarrollar un estilo más personal. Empecé a participar en mercados y a compartir mis piezas con el público, algo que me motiva a seguir creando. Hoy continúo experimentando, buscando un equilibrio entre lo funcional y lo estético, y disfrutando del proceso que hay detrás de cada pieza.
                        </p>                            
                    </div>                             
                </div>
            </div>          
        </div>
    );
}