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
                        Maribel García started working with ceramics in 2021, from a small room at home. During the first years, she made every piece entirely by hand, without a wheel or machinery, and she would take her work to different studios for firing. That stage helped her understand the process from the ground up and appreciate every detail of handmade craftsmanship. Later, she began learning wheel-throwing at Tierraviva, in Jerez—an experience that broadened her perspective on both the material and the craft.
                    </p>
                </div>
                <div className="about-block">
                    <p className="about-text">
                        Maribel García empezó en el mundo de la cerámica en 2021, trabajando desde una pequeña habitación de su casa. Durante los primeros años elaboraba cada pieza de forma completamente manual, sin torno ni maquinaria, y llevaba sus trabajos a distintos talleres para su cocción. Esa etapa le permitió conocer el proceso desde la base y valorar cada detalle del trabajo artesanal. Más adelante, tuvo la oportunidad de aprender torno en el taller Tierra Viva de Jerez, con la maestra Heyssel, una experiencia que amplió su forma de entender el material y la técnica.
                    </p>                          
                </div>
            </div>

            <div className="about-block">
                <img src="/images/About_2.jpeg" alt="Ceramic Affair Image" className="about-image-2" />
                <div>
                    <div className="about-block">
                        <p className="about-text">
                            Over time, she equipped her own studio and began incorporating specialized tools that opened up new possibilities. She also attended workshops with well-known ceramic artists, which helped her grow and develop a more personal style. She started taking her pieces to local markets and sharing them with others, something that continues to inspire her. Today, she keeps experimenting, searching for a balance between function and form, and enjoying the process behind every piece.
                        </p>
                    </div>

                    <div className="about-block">
                        <p className="about-text">
                            Con el tiempo fue equipando su propio taller y sumando herramientas que le permitieron explorar nuevas posibilidades. También realizó cursos con ceramistas reconocidos, lo que la ayudó a seguir creciendo y a desarrollar un estilo más personal. Empezó a participar en mercados y a compartir sus piezas con el público, algo que la motiva a seguir creando. Hoy continúa experimentando, buscando un equilibrio entre lo funcional y lo estético, y disfrutando del proceso que hay detrás de cada pieza.
                        </p>                          
                    </div>                             
                </div>
            </div>          
        </div>
    );
}